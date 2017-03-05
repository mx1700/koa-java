package me.mx1700.koalin.undertow

import me.mx1700.koalin.Cookie
import java.util.*

class Cookie(private val cookie: io.undertow.server.handlers.Cookie) : Cookie {
    override val name: String
        get() = cookie.name

    override var value: String
        get() = cookie.value
        set(value) { cookie.value = value }

    override var path: String?
        get() = cookie.path
        set(value) { cookie.path = value }

    override var domain: String?
        get() = cookie.domain
        set(value) { cookie.domain = value }

    override var secure: Boolean
        get() = cookie.isSecure
        set(value) { cookie.isSecure = value }

    override var httpOnly: Boolean
        get() = cookie.isHttpOnly
        set(value) { cookie.isHttpOnly = value }

    override var expires: Date?
        get() = cookie.expires
        set(value) { cookie.expires = value }

}
