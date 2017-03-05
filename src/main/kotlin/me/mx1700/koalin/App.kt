package me.mx1700.koalin

import me.mx1700.koalin.undertow.listen

fun main(args: Array<String>) {
    val app = Server()
    app.use { next ->
        res.body = "hello world!"
        res["X-Powered-By"] = "Koalin"
        next()
        println("""
${req.length}
${req.idempotent}
${req.hostName}
${req.host}
${req.charset}
${req.method}
${req.origin}
${req.path}
${req.protocol}
${req.queryString}
${req.secure}
${req.type}
${req.url}
""")
    }
    app.listen(9000, "0.0.0.0")
}



