package cz.kodytek.logic.services.interfaces

interface IMailService {

    fun sendPlainText(name: String, from: String, to: String, subject: String, body: String)

}