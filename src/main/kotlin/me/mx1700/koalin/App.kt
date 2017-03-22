package me.mx1700.koalin

import me.mx1700.koalin.jetty.Resource
import me.mx1700.koalin.jetty.listen

fun main(args: Array<String>) {
    val app = Server()

    app.use(Resource())

    app.use {
        response.body = "hello world! 你好，世界！"
        response.status = 200
        next()
        println("""
${request.length}
${request.idempotent}
${request.hostname}
${request.host}
${request.charset}
${request.method}
${request.origin}
${request.path}
${request.protocol}
${request.queryString}
${request.secure}
${request.type}
${request.url}
${request.href}
""")
    }

    app.use {
        val body = response.body
        if (body is String) {
            response.body = body + " next!"
        }
    }

    app.listen(9000)
}



