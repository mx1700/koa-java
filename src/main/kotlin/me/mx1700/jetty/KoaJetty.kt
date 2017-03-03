package me.mx1700.jetty

import mx1700.koa.Koa
import org.eclipse.jetty.server.Server

/**
 * 启动 server ，监听指定端口
 */
fun Koa.listen(port: Int) {
    val server = Server(port)
    val handle = HttpHandler(callback)
    server.handler = handle
    server.start()
//    server.dumpStdErr()
    server.join()
}
