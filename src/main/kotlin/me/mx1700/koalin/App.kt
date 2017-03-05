package me.mx1700.koalin

import me.mx1700.koalin.undertow.listen

fun main(args: Array<String>) {
    val app = Server()
    app.use { next ->
        res.body = "hello world!"
        res["X-Powered-By"] = "Koalin"
        next()
    }
    app.listen(9000, "0.0.0.0")
}



