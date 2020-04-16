package cz.kodytek.logic.mappers.interfaces

interface IMapper<I, O> {

    fun mapTo(value: I): O

    fun mapFrom(value: O): I

}
