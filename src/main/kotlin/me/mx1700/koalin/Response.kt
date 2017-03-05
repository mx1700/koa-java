package me.mx1700.koalin

import java.util.*

interface Response {

    /**
     * socket 对象
     */
    val socket: String
    get() = TODO()

    /**
     * 响应头
     */
    val headers: Headers

    /**
     * status code
     */
    var status: Int

    /**
     * status message
     */
    var message: String?

    /**
     * cookies
     */
    val cookies: Cookies

    /**
     * 响应体
     */
    var body: String?

    /**
     * 响应长度
     */
    var length: Long

    /**
     * 头是否已发送
     */
    val headerSent: Boolean

    /**
     * 不明白
     */
    fun vary(name: String): Unit = TODO()

    /**
     * redirect
     */
    fun redirect(url: String)

    /**
     * et Content-Disposition header to "attachment" with optional `filename`.
     */
    fun attachment(fileName: String): Unit = TODO()

    /**
     * 相应类型
     */
    var type: String?

    /**
     * Last-Modified
     */
    var lastModified: Date?

    /**
     * etag
     */
    var etag: String?

    /**
     * 获取头
     */
    operator fun get(head: String): String?

    /**
     * 设置头
     */
    operator fun set(head: String, value: String?)

    /**
     * 删除头
     */
    fun remove(head: String)

    /**
     * 是否可继续写入
     */
    val writable: Boolean

    /**
     * 发送头
     */
    fun flushHeaders()
}
