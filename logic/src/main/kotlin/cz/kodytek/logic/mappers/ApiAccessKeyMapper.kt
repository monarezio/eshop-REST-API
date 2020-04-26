package cz.kodytek.logic.mappers

import cz.kodytek.logic.mappers.interfaces.IMapper
import cz.kodytek.logic.models.ApiAccessKey
import cz.kodytek.eshop.data.entities.ApiAccessKey as DbApiAccessKey

object ApiAccessKeyMapper: IMapper<DbApiAccessKey, ApiAccessKey> {
    override fun mapTo(value: DbApiAccessKey): ApiAccessKey {
        return ApiAccessKey(value.email, value.token)
    }

    override fun mapFrom(value: ApiAccessKey): DbApiAccessKey {
        return DbApiAccessKey(null, value.email, value.key)
    }
}