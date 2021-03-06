package me.mx1700.koalin

import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

class Request(private val ctx: Context) {

    private val req = ctx.req

    /**
     * 原生 ServletRequest 对象
     */
    val raw = req

    /**
     * 请求头字典
     */
    val headers = Headers(req)

    /**
     * 请求 url
     */
    val url: String = req.requestURL.toString()

    /**
     * 完整请求地址
     */
    val href: String by lazy {
        if (queryString.isNullOrEmpty())
            url
        else
            "$url?$queryString"
    }

    /**
     * 请求源
     */
    val origin: String by lazy {
        "${req.scheme}://${req.remoteHost}"
    }

    /**
     * scheme
     */
    val scheme: String = req.scheme

    /**
     * 请求 method
     */
    val method: String = req.method

    /**
     * pathname
     */
    val path: String = req.pathInfo

    /**
     * query 字典
     */
    val query: Map<String, String> by lazy {
        req.parameterNames.asSequence().map { it to req.getParameter(it) }.toMap()
    }


    /**
     * query string
     */
    val queryString: String? = req.queryString

    /**
     * cookies
     */
    val cookies: Map<String, Cookie> by lazy {
        req.cookies.map { it.name to it }.toMap()
    }

    /**
     * "head" 头 带端口
     */
    val host: String by lazy {
        if (ctx.app.proxy && this["X-Forwarded-Host"] != null)
            this["X-Forwarded-Host"]!!.split("\\s*,\\s*".toRegex())[0]
        else
            "${this.hostname}:${req.serverPort}"
    }

    /**
     * "head" 头
     */
    val hostname: String by lazy {
        if (ctx.app.proxy && this["X-Forwarded-Host"] != null) {
            this.host.split(':')[0]
        } else {
            req.remoteHost
        }
    }

    /**
     * 检查是否是新的请求
     * Last-Modified and/or the ETag
     */
    val fresh: Boolean
        get() {
            if (method != "GET" && method != "HEAD") {
                return false
            }
            val s = ctx.res.status
            val req = ctx.request
            val res = ctx.response

            // 2xx or 304 as per rfc2616 14.26
            if ((s in 200..299) || 304 == s) {
                var etagMatches = true;
                var notModified = true;

                val modifiedSince = req["if-modified-since"]
                val noneMatch = req["if-none-match"]
                val lastModified = res["last-modified"]
                val etag = res["etag"]
                val cc = req["cache-control"]

                // unconditional request
                if (modifiedSince == null && noneMatch == null) return false

                // check for no-cache cache request directive
                if (cc != null && cc.indexOf("no-cache") != -1) return false

                // if-none-match
                if (noneMatch != null) {
                    etagMatches = noneMatch.split(" *, *".toRegex())
                            .any { it == "*" || it == etag || it == "w/" + etag }
                }

                // if-modified-since
                if (modifiedSince != null && lastModified != null) {
                    notModified = TimeUtil.stringToDate(lastModified) <= TimeUtil.stringToDate(modifiedSince);
                }

                return etagMatches && notModified
            }

            return false
        }

    /**
     * 检查是否是旧的请求
     */
    val stale: Boolean
        get() = !fresh

    /**
     * 请求是否幂等
     */
    val idempotent: Boolean by lazy {
        method in listOf("GET", "HEAD", "PUT", "DELETE", "OPTIONS", "TRACE")
    }

    /**
     * 字符集
     */
    val charset: String? = req.characterEncoding

    /**
     * 请求数据长度
     */
    val length: Long? = req.contentLengthLong

    /**
     * 协议
     */
    val protocol: String by lazy {
        when {
            req.isSecure -> "https"
            !ctx.app.proxy -> "http"
            this["X-Forwarded-Proto"] != null ->
                this["X-Forwarded-Proto"]!!.split("\\s*,\\s*".toRegex())[0]
            else -> "http"
        }
    }

    /**
     * 是否是安全连接
     */
    val secure: Boolean by lazy {
        protocol == "https"
    }

    /**
     * When `app.proxy` is `true`, parse
     * the "X-Forwarded-For" ip address list.
     *
     * For example if the value were "client, proxy1, proxy2"
     * you would receive the array `["client", "proxy1", "proxy2"]`
     * where "proxy2" is the furthest down-stream.
     *
     * @return {Array}
     * @api public
     */
    val ips: Iterable<String> by lazy {
        if (ctx.app.proxy && this["X-Forwarded-For"] != null)
            this["X-Forwarded-For"]!!.split("\\s*,\\s*".toRegex())
        else
            emptyList()
    }


    /**
     * 检查是否是支持的类型
     * TODO: 暂未实现
     */
//    fun accepts(vararg accepts: String): Boolean
//    val acceptsEncodings: String
//    val acceptsCharsets: String
//    val acceptsLanguages: String
//    fun isType(vararg types: String) : Boolean

    /**
     * 获取 mine type
     */
    val type: String? = req.contentType

    /**
     * 获取 header
     */
    operator fun get(head: String): String? = req.getHeader(head)


    class Headers(private val req: HttpServletRequest): Iterable<Pair<String, Cookie>> {

        override fun iterator(): Iterator<Pair<String, Cookie>> {
            return req.cookies.map { it.name to it }.iterator()
        }

        operator fun get(name: String): String = req.getHeader(name)
        fun getValues(name: String): Enumeration<String>? = req.getHeaders(name)!!
        fun getInt(name: String) = req.getIntHeader(name)
        fun getDate(name: String) = req.getDateHeader(name)
    }
}