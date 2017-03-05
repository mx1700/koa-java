package me.mx1700.koalin

import java.util.*

interface Cookie {
    val name: String
    var value: String
    var path: String?
    var domain: String?
    var secure: Boolean
    var httpOnly: Boolean
    var expires: Date?
}
