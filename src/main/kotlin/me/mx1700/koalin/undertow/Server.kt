package me.mx1700.koalin.undertow

import io.undertow.Undertow
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import io.undertow.util.Headers
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import me.mx1700.koalin.Request
import me.mx1700.koalin.Response
import me.mx1700.koalin.Server
import java.util.concurrent.Executor

fun Server.listen(port: Int, host: String = "localhost") {

    val server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(MyHandler(this.callback)).build();
        server.start();
}

class MyHandler(private val _run: suspend (Request, Response) -> Unit) : HttpHandler {
    override fun handleRequest(exchange: HttpServerExchange) {
        exchange.dispatch(Executor { it.run() }, Runnable {
            launch(CommonPool) {
                val req = me.mx1700.koalin.undertow.Request(exchange)
                val res = me.mx1700.koalin.undertow.Response(exchange)
                _run(req, res)
                if (res.body != null) {
                    exchange.responseSender.send(res.body)
                } else {
                    exchange.endExchange()
                }
            }
        })
    }
}