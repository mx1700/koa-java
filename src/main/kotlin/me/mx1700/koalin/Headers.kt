package me.mx1700.koalin

interface Headers : Iterable<Pair<String, String>> {
    operator fun get(name: String): String
    operator fun set(name: String, value: String)
    fun remove(name: String)
    val length: Int
}
