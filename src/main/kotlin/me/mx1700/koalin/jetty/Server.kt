package me.mx1700.koalin.jetty

import org.eclipse.jetty.server.Server;
import me.mx1700.koalin.Server as KoaServer

fun KoaServer.listen(port: Int = 9000) {
    val server = Server(port);
    server.handler = Handler(this.callback)
    server.start();
    //server.dumpStdErr();
    server.join();
}