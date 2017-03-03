package me.mx1700

import me.mx1700.jetty.listen
import mx1700.koa.Koa
import test.test

fun main(args: Array<String>) {

    val app = Koa()

    app.use {
        request.headers.map {(key, value) ->
            println("$key: $value")
        }
    }

    app.listen(9000)
    app.test()
}

//class Header {
//    operator fun get(name: String) = name
//    fun getName(name:String) = name
//}
