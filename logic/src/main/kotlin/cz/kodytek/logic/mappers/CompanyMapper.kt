package cz.kodytek.logic.mappers

import cz.kodytek.eshop.data.entities.Company as DbCompany
import cz.kodytek.logic.mappers.interfaces.IMapper
import cz.kodytek.logic.models.invoice.Company

object CompanyMapper : IMapper<DbCompany?, Company?> {

    override fun mapTo(value: DbCompany?): Company? {
        return if (value == null) null
        else Company(value.name, value.identificationNumber, value.taxIdentificationNumber, AddressMapper.mapTo(value.address))
    }

    override fun mapFrom(value: Company?): DbCompany? {
        return if (value == null) null
        else DbCompany(null, value.name!!, value.identificationNumber!!, value.taxIdentificationNumber, AddressMapper.mapFrom(value.address!!))
    }
}