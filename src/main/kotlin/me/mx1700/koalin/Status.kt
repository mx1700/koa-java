package me.mx1700.koalin

object Status {

    fun empty(code: Int) = when (code) {
        204, 205, 304 -> true
        else -> false
    }

    fun redirect(code: Int) = when (code) {
        300, 301, 302, 303, 305, 307, 308 -> true
        else -> false
    }

    fun retry(code: Int) = when (code) {
        502, 503, 504 -> true
        else -> false
    }

}
