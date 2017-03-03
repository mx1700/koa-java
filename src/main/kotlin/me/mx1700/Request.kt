package me.mx1700

import java.util.*
import javax.servlet.http.HttpServletRequest

class Request(private val req: HttpServletRequest) {

    val headers = Headers(req)

    fun test() {
        val a = this.headers["aaa"]
    }
}

class Headers(private val req: HttpServletRequest) : Iterable<Pair<String, String>> {

    override fun iterator(): Iterator<Pair<String, String>> =
            req.headerNames.asSequence().map { it to req.getHeader(it) }.iterator()

    operator fun get(name: String) = req.getHeader(name)
}