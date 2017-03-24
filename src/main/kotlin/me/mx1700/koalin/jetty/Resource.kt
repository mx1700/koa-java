package me.mx1700.koalin.jetty

import me.mx1700.koalin.Context
import me.mx1700.koalin.Middleware
import org.eclipse.jetty.http.MimeTypes
import org.eclipse.jetty.server.ResourceContentFactory
import org.eclipse.jetty.server.ResourceService
import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.util.resource.ResourceFactory
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 资源文件处理
 */
class Resource : Middleware {
    private val _mimeTypes = MimeTypes()
    private val _resourceService = object: ResourceService() {
        override fun notFound(request: HttpServletRequest?, response: HttpServletResponse?) {
            //未找到文件则不处理
        }
    }

    init {
        _resourceService.contentFactory = ResourceContentFactory(ResourceFactory { path ->
            Resource.newResource(path)
        }, _mimeTypes, _resourceService.precompressedFormats)
    }
    
    override fun invoke(ctx: Context) {
        val request = ctx.req
        val response = ctx.res
        _resourceService.doGet(request, response)
        if (!response.isCommitted) {
            ctx.next()
        }
    }

}
