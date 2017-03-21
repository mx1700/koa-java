package me.mx1700.koalin

import me.mx1700.koalin.Cookie
import java.util.*
import javax.servlet.http.HttpServletResponse

class Cookies(private val res: HttpServletResponse) : Iterable<Cookie> {

    val cookiesMap = HashMap<String, Cookie>()
    operator fun get(name: String): Cookie? = cookiesMap[name]
    operator fun set(name: String, value: String?) {
        this.set(name, value, "/")
    }
    fun set(name: String, value: String?,
            path: String? = "/", domain: String? = null,
            secure: Boolean = false, httpOnly: Boolean = true,
            maxAge: Int? = null) {

        val cookie = Cookie(name, value, path, domain, secure, httpOnly, maxAge)
        cookiesMap[name] = cookie

        val sCookie = javax.servlet.http.Cookie(name, value)
        if (path != null) sCookie.path = path
        if (domain != null) sCookie.domain = domain
        sCookie.secure = secure
        sCookie.isHttpOnly = httpOnly
        if (maxAge != null) sCookie.maxAge = maxAge
        res.addCookie(sCookie)
    }

    override fun iterator(): Iterator<Cookie> {
        return cookiesMap.values.iterator()
    }
}
