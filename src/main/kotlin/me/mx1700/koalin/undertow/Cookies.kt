package me.mx1700.koalin.undertow

import io.undertow.server.handlers.CookieImpl
import me.mx1700.koalin.Cookie
import me.mx1700.koalin.Cookies
import java.util.*

class Cookies(private val cookies: MutableMap<String, io.undertow.server.handlers.Cookie>) : Cookies {
    override fun get(name: String): Cookie? {
        val cookie = cookies[name]
        return if (cookie == null) null else me.mx1700.koalin.undertow.Cookie(cookie)
    }

    override fun add(name: String, value: String,
            path: String?, domain: String?,
            secure: Boolean, httpOnly: Boolean,
                     expires: Date?) {

        val cookie = CookieImpl(name, value)
        cookie.path = path
        cookie.domain = domain
        cookie.isSecure = secure
        cookie.isHttpOnly = httpOnly
        cookie.expires = expires

        cookies.put(cookie.name, cookie)
    }

    override fun remove(name: String) {
        val cookie = cookies[name]
        if (cookie != null) {
            cookie.isDiscard = true
        }
    }

    override val length: Int
        get() = cookies.size

    override fun iterator(): Iterator<Pair<String, Cookie>> {
        return cookies.asSequence().map { (name, cookie) -> name to me.mx1700.koalin.undertow.Cookie(cookie) }.iterator()
    }
}