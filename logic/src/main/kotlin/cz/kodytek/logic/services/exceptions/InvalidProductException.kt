package cz.kodytek.logic.services.exceptions

class InvalidProductException(val products: List<Long>) : Exception()