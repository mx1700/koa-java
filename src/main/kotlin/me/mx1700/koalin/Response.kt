package me.mx1700.koalin

import java.util.*
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection


class Response(private val ctx: Context) {

    val res = ctx.res

    /**
     * 原生对象
     */
    val raw = res

    /**
     * 响应头
     */
    val headers: Headers = Headers(res)

    /**
     * status code
     */
    var status: Int
        get() = res.status
        set(value) {
            res.status = value
        }

    /**
     * cookies
     */
    val cookies: Cookies = Cookies(res)

    /**
     * 响应体
     */
    var body: String? = null

    /**
     * 添加 vary 头
     * TODO: 未实现
     */
    //    fun vary(name: String): Unit = TODO()

    /**
     * 302 redirect
     */
    fun redirect(url: String): Unit {
        this.res.sendRedirect(url)
    }

    /**
     * set Content-Disposition header to "attachment" with optional `filename`.
     */
    fun attachment(fileName: String): Unit {
        this.type = fileName
        this["Content-Disposition"] = "attachment; filename=\"$fileName\""
    }

    /**
     * 响应类型
     */
    var type: String?
        get() = res.contentType
        set(value) {
            if (value == null || value.indexOf("/") >= 0) {
                res.contentType = value
            } else {
                val fileNameMap = URLConnection.getFileNameMap()
                res.contentType = fileNameMap.getContentTypeFor(value)
            }
        }

    /**
     * Last-Modified
     */
    var lastModified: Date?
        get() {
            val last = this["Last-Modified"]
            return if (last != null) TimeUtil.stringToDate(last) else null
        }
        set(value) {
            this["Last-Modified"] = if (value != null) TimeUtil.dateToString(value) else null
        }

    /**
     * etag
     */
    var etag: String?
        get() = this["ETag"]
        set(value) {
            if (value != null && !"^(W/)?\".*".toRegex().matches(value)) {
                this["Etag"] = "\"$value\""
            } else {
                this["Etag"] = value
            }
        }

    /**
     * 获取头
     */
    operator fun get(head: String): String? = headers[head]

    /**
     * 设置头
     */
    operator fun set(head: String, value: String?): Unit {
        if (value != null) {
            headers[head] = value
        } else {
            headers.remove(head)
        }
    }

    /**
     * 删除头
     */
    fun remove(head: String): Unit = headers.remove(head)


    class Headers(private val res: HttpServletResponse) : Iterable<Pair<String, String>> {
        override fun iterator(): Iterator<Pair<String, String>> {
            return res.headerNames.map { it to res.getHeader(it) }.iterator()
        }

        operator fun get(name: String): String? {
            return res.getHeader(name)
        }

        operator fun set(name: String, value: String) {
            res.setHeader(name, value)
        }

        fun remove(name: String) {
            res.setHeader(name, null)
        }

        fun contains(name: String) = res.containsHeader(name)

        val length: Int
            get() = res.headerNames.size
    }
}

