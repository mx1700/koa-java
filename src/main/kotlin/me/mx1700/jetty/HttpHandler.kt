package me.mx1700.jetty

import java.io.PrintWriter
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler


class HttpHandler(private val run: (request: HttpServletRequest,
                                    response: HttpServletResponse) -> Unit) : AbstractHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun handle(target: String,
                        baseRequest: Request,
                        request: HttpServletRequest,
                        response: HttpServletResponse) {

        response.contentType = "text/html; charset=utf-8"
        response.status = HttpServletResponse.SC_NOT_FOUND

        run(request, response)

        baseRequest.isHandled = true
    }
}
