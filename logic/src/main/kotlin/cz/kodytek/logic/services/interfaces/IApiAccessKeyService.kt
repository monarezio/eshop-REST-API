package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.ApiAccessKey

interface IApiAccessKeyService {

    fun create(email: String): ApiAccessKey

    fun getAll(): List<ApiAccessKey>

    fun get(key: String): ApiAccessKey

    fun log(key: String, url: String, ip: String, body: String)

}