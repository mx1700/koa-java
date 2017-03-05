package me.mx1700.koalin

import java.util.*

interface Request {

    /**
     * 请求头列表
     */
    val headers: Headers

    /**
     * 请求 url
     */
    val url: String

    /**
     * 完整请求地址
     */
    val href: String

    /**
     * 请求源
     * ${this.String}://${this.host}
     */
    val origin: String

    /**
     * scheme
     */
    val scheme: String

    /**
     * 请求 method
     */
    val method: String

    /**
     * pathname
     */
    val path: String

    /**
     * 解析后的 query
     */
    val query: Map<String, String>

    /**
     * query string
     */
    val queryString: String

    /**
     * cookies
     */
    val cookies: Cookies

    /**
     * "head" 头 带端口
     * 支持 X-Forwarded-Host 当 proxy 打开
     */
    val host: String

    /**
     * "head" 头
     * 支持 X-Forwarded-Host 当 proxy 打开
     */
    val hostName: String

    /**
     * 检查是否是新的请求
     * Last-Modified and/or the ETag
     */
    val fresh: Boolean

    /**
     * 检查是否是旧请求
     */
    val stale: Boolean

    /**
     * 请求是否幂等
     */
    val idempotent: Boolean

    /**
     * 字符集
     */
    val charset: String?

    /**
     * 请求数据长度
     */
    val length: Long?

    /**
     * 协议
     * 支持 X-Forwarded-Proto
     */
    val protocol: String

    /**
     * 是否是安全连接
     */
    val secure: Boolean

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
    val ips: Iterable<String>


//    /**
//     * 检查是否是支持的类型
//     */
//    fun accepts(vararg accepts: String): Boolean

//    val acceptsEncodings: String    //很复杂
//    val acceptsCharsets: String     //很复杂
//    val acceptsLanguages: String    //很复杂

//    /**
//     *
//     */
//    fun isType(vararg types: String) : Boolean

    /**
     * 获取 mine type
     */
    val type: String?

    /**
     * 获取 header
     */
    operator fun get(head: String): String


}