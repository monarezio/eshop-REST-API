package cz.kodytek.logic.mappers

import cz.kodytek.eshop.data.entities.Address as DbAddress
import cz.kodytek.logic.mappers.interfaces.IMapper
import cz.kodytek.logic.models.invoice.Address

object AddressMapper : IMapper<DbAddress, Address> {
    override fun mapTo(value: DbAddress): Address = Address(value.city, value.street, value.postcode)

    override fun mapFrom(value: Address): DbAddress = DbAddress(null, value.city!!, value.street!!, value.postcode!!)
}