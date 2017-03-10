package me.mx1700.koalin.undertow

import io.undertow.server.HttpServerExchange
import io.undertow.util.HeaderMap
import io.undertow.util.Headers
import io.undertow.util.HttpString
import me.mx1700.koalin.Request

class Request(private val exchange: HttpServerExchange) : Request {

    override val headers = RequestHeaders(exchange.requestHeaders)

    override val url
        get() = exchange.requestURL!!

    override val href: String
        get() = "$url$queryString"

    override val origin: String
        get() = "${this.scheme}://${this.host}"

    override val scheme: String
        get() = exchange.requestScheme

    override val method
        get() = exchange.requestMethod.toString()

    override val path: String
        get() = exchange.requestPath

    override val query
        get() = exchange.queryParameters.map { item -> item.key to item.value.first() }.toMap()

    override val cookies = Cookies(exchange.requestCookies)

    override val queryString: String
        get() = exchange.queryString

    override val host: String
        get() = exchange.hostAndPort

    override val hostName: String
        get() = exchange.hostName

    override val fresh: Boolean
        get() = TODO("not implemented")

    override val stale: Boolean
        get() = !fresh

    override val idempotent: Boolean
        get() = this.method in listOf("GET", "HEAD", "PUT", "DELETE", "OPTIONS", "TRACE")

    override val charset: String?
        get() = exchange.requestCharset

    override val length
        get() = exchange.requestContentLength

    override val protocol
        get() = exchange.protocol.toString()

    override val secure
        get() = scheme == "https"

    override val ips: Iterable<String>
        get() = TODO("not implemented")

    override val type: String?
        get() = this[Headers.CONTENT_TYPE]

    override fun get(head: String): String {
        return exchange.requestHeaders.getFirst(head)
    }
    operator fun get(name: HttpString): String? = exchange.requestHeaders[name]?.firstOrNull()

    class RequestHeaders(private val map: HeaderMap) : me.mx1700.koalin.Headers {
        override fun get(name: String): String {
            return map.getFirst(name)
        }

        override fun set(name: String, value: String) {
            map.put(HttpString(name), value)
        }

        override fun remove(name: String) {
            map.remove(name)
        }

        override val length: Int
            get() = map.size()

        override fun iterator(): Iterator<Pair<String, String>> {
            return map.headerNames.asSequence().map { it.toString() to map.getFirst(it) }.iterator()
        }
    }
}
