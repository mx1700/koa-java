package me.mx1700.koalin.undertow

import io.undertow.server.HttpServerExchange
import io.undertow.util.HeaderMap
import io.undertow.util.HttpString
import io.undertow.util.Headers
import me.mx1700.koalin.Response
import java.text.SimpleDateFormat
import java.util.*

class Response(private val exchange: HttpServerExchange) : Response {

    companion object {
        private val lastModifiedFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        init {
            lastModifiedFormat.timeZone = TimeZone.getTimeZone("GTM")
        }
    }

    override val headers = ResponseHeaders(exchange.responseHeaders)

    override var status: Int
        get() = exchange.statusCode
        set(value) { exchange.statusCode = value }

    override var message: String?
        get() = exchange.reasonPhrase
        set(value) { exchange.reasonPhrase = value }

    override var body: String? = null

    override var length: Long
        get() = exchange.responseContentLength
        set(value) { exchange.responseContentLength = value }

    override val headerSent: Boolean
        get() = TODO("not implemented")

    override fun redirect(url: String) {
        TODO("not implemented")     //TODO:未完成
    }

    override var type: String?
        get() = this[Headers.CONTENT_TYPE]
        set(value) { this[Headers.CONTENT_TYPE] = value }

    override var lastModified: Date?
        get() = lastModifiedFormat.parse(this[Headers.LAST_MODIFIED])
        set(value) {
            this[Headers.LAST_MODIFIED] = lastModifiedFormat.format(value)

        }

    override var etag: String?
        get() = this[Headers.ETAG]
        set(value) {
            this[Headers.ETAG] = value
        }

    override val cookies = Cookies(exchange.responseCookies)

    override fun get(head: String): String? = exchange.responseHeaders.getFirst(head)
    operator fun get(name: HttpString): String? = exchange.responseHeaders[name].first

    override fun set(head: String, value: String?) {
        if (value == null) {
            remove(head)
        } else {
            exchange.responseHeaders.put(HttpString(head), value)
        }
    }

    operator fun set(name: HttpString, value: String?) {
        if (value == null) {
            exchange.responseHeaders.remove(name)
        } else {
            exchange.responseHeaders.put(name, value)
        }
    }

    override fun remove(head: String) {
        exchange.responseHeaders.remove(head)
    }

    override val writable: Boolean
        get() = exchange.isComplete

    override fun flushHeaders() {
        TODO("not implemented")
    }

    class ResponseHeaders(private val map: HeaderMap) : me.mx1700.koalin.Headers {
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
