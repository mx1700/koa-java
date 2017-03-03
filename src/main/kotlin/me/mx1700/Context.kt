package me.mx1700

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Context(val req: HttpServletRequest, val resp: HttpServletResponse) {
    val request = Request(req)
    val response = Response(resp)
}
