package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.eshop.data.entities.Log
import cz.kodytek.logic.mappers.ApiAccessKeyMapper
import cz.kodytek.logic.models.ApiAccessKey
import cz.kodytek.logic.services.interfaces.IApiAccessKeyService
import org.hibernate.Session
import java.time.LocalDateTime
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Path
import cz.kodytek.eshop.data.entities.ApiAccessKey as DbApiAccessKey

@ApplicationScoped
open class ApiAccessKeyService : IApiAccessKeyService {
    override fun create(email: String): ApiAccessKey = HibernateSession.createSession { s ->
        println("Email: " + email)
        ApiAccessKeyMapper.mapTo(
                s.saveAndGet(DbApiAccessKey(null, email, UUID.randomUUID().toString()))
        )
    }

    override fun getAll(): List<ApiAccessKey> = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbApiAccessKey::class.java)
        val root = cq.from(DbApiAccessKey::class.java)

        s.createQuery(cq).resultList.map { ApiAccessKeyMapper.mapTo(it) }
    }

    override fun get(key: String): ApiAccessKey = HibernateSession.createSession { s ->
        ApiAccessKeyMapper.mapTo(s.createQuery(getRawApiAccessKey(s, key)).singleResult)
    }

    override fun log(key: String, url: String, ip: String, body: String) = HibernateSession.createSession { s ->
        val apiAccessKey = s.createQuery(getRawApiAccessKey(s, key)).singleResult
        val log = s.saveAndGet(Log(null, url, body, ip, LocalDateTime.now()))
        apiAccessKey.logs.add(log)
        Unit
    }

    private fun getRawApiAccessKey(s: Session, key: String): CriteriaQuery<DbApiAccessKey> {
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbApiAccessKey::class.java)
        val root = cq.from(DbApiAccessKey::class.java)

        val path: Path<DbApiAccessKey> = root.get("token")
        cq.where(cb.equal(path, key))

        return cq
    }
}