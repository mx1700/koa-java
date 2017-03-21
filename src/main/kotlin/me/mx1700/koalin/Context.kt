package me.mx1700.koalin

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Context(
        val app: Server,
        internal val req: HttpServletRequest,
        internal val res: HttpServletResponse) {
    val request: Request = Request(this)
    val response: Response = Response(this)
    lateinit var next: Next

}
