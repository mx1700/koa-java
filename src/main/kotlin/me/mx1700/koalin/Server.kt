package me.mx1700.koalin

import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

typealias Next = () -> Unit
typealias Middleware = (Context) -> Unit
typealias OnException = Context.(Exception) -> Unit

class Server {

    /**
     * callback 的引用
     */
    val callback: (HttpServletRequest, HttpServletResponse) -> Unit = this::callback

    /**
     * 中间件列表
     */
    private val middlewareList = arrayListOf<Middleware>()

    /**
     * 异常事件
     */
    private var onExceptionAction : OnException? = null

    /**
     * 添加中间件
     */
    fun use(middleware: Context.() -> Unit) {
        middlewareList.add(middleware)
    }

    /**
     * 使用并配置中间件
     */
    fun <T: Middleware> use(middleware: T, config: T.() -> Unit) {
        config(middleware)
        middlewareList.add(middleware)
    }

    /**
     * 开始执行中间件
     */
    private fun callback(request: HttpServletRequest,
                    response: HttpServletResponse) {
        val ctx = Context(this, request, response)
        next(0, ctx)
        respond(ctx)
    }

    /**
     * 执行下一个中间件
     */
    private fun next(index: Int, ctx: Context) {
        if (index == middlewareList.count()) {
            return
        }
        val middleware = middlewareList[index]
        try {
            ctx.next =  {
                next(index + 1, ctx)
            }
            middleware(ctx)
        } catch (err: Exception) {
            if (onExceptionAction != null) {
                onExceptionAction?.invoke(ctx, err)
            } else {
                throw err
            }
        }
    }

    /**
     * 异常事件
     */
    fun onException(action: OnException) {
        onExceptionAction = action
    }

    /**
     * 输出 response
     */
    private fun respond(ctx: Context) {
        val res = ctx.response.raw
        val body = ctx.response.body

        if (body != null) {
            res.characterEncoding = "UTF-8";
            res.writer.print(body)
        }
    }
}
