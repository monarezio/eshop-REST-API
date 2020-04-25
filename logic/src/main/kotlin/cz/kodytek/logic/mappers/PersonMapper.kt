package cz.kodytek.logic.mappers

import cz.kodytek.eshop.data.entities.Person as DbPerson
import cz.kodytek.logic.mappers.interfaces.IMapper
import cz.kodytek.logic.models.invoice.Person

object PersonMapper : IMapper<DbPerson, Person> {
    override fun mapTo(value: DbPerson): Person = Person(value.fullName, AddressMapper.mapTo(value.address))

    override fun mapFrom(value: Person): DbPerson = DbPerson(null, value.fullName!!, AddressMapper.mapFrom(value.address!!))
}