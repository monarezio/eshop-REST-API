package cz.kodytek.logic.reader

import java.io.File
import javax.enterprise.context.ApplicationScoped
import kotlin.streams.toList

@ApplicationScoped
open class CSVReader {

    open fun <T> read(fileName: String, parser: (String) -> T): List<T> {
        return Thread.currentThread().contextClassLoader
                .getResourceAsStream(fileName)!!
                .bufferedReader()
                .lines()
                .map { parser(it) }
                .toList()
    }

}