package cz.kodytek.eshop.data.connections

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
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

        return result!!
    }

    private fun getSessionFactory(): SessionFactory? {
        if (sessionFactory == null) {

            // configures settings from hibernate.cfg.xml
            val registry = StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build()
            try {
                sessionFactory = MetadataSources(registry)
                        .buildMetadata()
                        .buildSessionFactory()
            } catch (e: Exception) {
                e.printStackTrace()
                StandardServiceRegistryBuilder.destroy(registry)
            }
        }
        return sessionFactory
    }

    private fun getEntityManager(): EntityManager? {
        if (entityManagerFactory == null) entityManagerFactory = Persistence.createEntityManagerFactory("eshop")
        return entityManagerFactory!!.createEntityManager()
    }
}
