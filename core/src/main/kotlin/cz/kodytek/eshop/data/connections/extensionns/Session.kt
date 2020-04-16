package cz.kodytek.eshop.data.connections.extensionns

import org.hibernate.Session

fun<T> Session.saveAndGet(entity: T): T {
    save(entity)
    return entity
}
