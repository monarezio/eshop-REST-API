package cz.kodytek.logic.services

import com.sendgrid.*
import cz.kodytek.logic.services.interfaces.IMailService
import java.io.IOException
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
open class MailService : IMailService {

    override fun sendPlainText(name: String, from: String, to: String, subject: String, body: String) {
        val content = Content("text/plain", body)
        val mail = Mail(Email(from, name), subject, Email(to), content)

        val sg = SendGrid(getApiKey())
        val request = Request()
        try {
            request.setMethod(Method.POST)
            request.setEndpoint("mail/send")
            request.setBody(mail.build())
            val response: Response = sg.api(request)
            System.out.println(response.getStatusCode())
            System.out.println(response.getBody())
            System.out.println(response.getHeaders())
        } catch (ex: IOException) {
            throw ex
        }
    }

    private fun getApiKey(): String {
        return Thread.currentThread().contextClassLoader
                .getResourceAsStream("sendgrid.key")!!
                .bufferedReader().readLine()
    }

}