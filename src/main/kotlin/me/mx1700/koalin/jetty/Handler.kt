package me.mx1700.koalin.jetty

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Handler(private val _run: (HttpServletRequest, HttpServletResponse) -> Unit) : AbstractHandler() {
    override fun handle(target: String?, baseRequest: Request,
                        request: HttpServletRequest, response: HttpServletResponse) {
        _run(request, response)
        baseRequest.isHandled = true
    }
}
