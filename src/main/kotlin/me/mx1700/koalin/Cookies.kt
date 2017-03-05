package me.mx1700.koalin

import java.util.*

interface Cookies : Iterable<Pair<String, Cookie>> {
    operator fun get(name: String): Cookie?

    fun add(name: String, value: String,
            path: String? = null, domain: String? = null,
            secure: Boolean = false, httpOnly: Boolean = true,
            expires: Date? = null)

    fun remove(name: String)

    val length: Int
}
