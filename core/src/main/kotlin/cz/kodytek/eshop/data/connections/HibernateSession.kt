package cz.kodytek.eshop.data.connections

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException
import javax.persistence.Persistence

object HibernateSession {
    private var entityManagerFactory: EntityManagerFactory? = null

    private var sessionFactory: SessionFactory? = null

    fun <T> createSession(fn: (Session) -> T): T {
        var result: T? = null
        var transaction: Transaction? = null

        try {
            getEntityManager()!!.unwrap(Session::class.java).use { session ->
                transaction = session.beginTransaction()
                result = fn(session)
                transaction?.commit()
            }
        } catch (e: Exception) {
            try {
                transaction?.rollback()
            } catch (ignored: IllegalStateException) {
            }
            throw e
        }

        return result ?: throw NoResultException()
    }

    private fun getEntityManager(): EntityManager? {
        if (entityManagerFactory == null) entityManagerFactory = Persistence.createEntityManagerFactory("eshop")
        return entityManagerFactory!!.createEntityManager()
    }
}
