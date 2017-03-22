package me.mx1700.koalin

import org.slf4j.LoggerFactory
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

typealias Next = () -> Unit
typealias Middleware = (Context) -> Unit
typealias OnException = Context.(Exception) -> Unit

class Server(
        /**
         * 是否是通过代理访问，当设置为 true 的时候，会通过代理头获取 ip 和 host
         */
        val proxy: Boolean = false) {

    val name = "Koalin"
    /**
     * 日志类
     */
    val logger = LoggerFactory.getLogger(name)!!

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
    private var onExceptionAction : OnException? = { e ->
        logger.error("request exception", e)
        response.status = 500
        res.characterEncoding = "UTF-8"
        e.printStackTrace(res.writer)
        response.body = null
    }

    private val errorHandler: OnException = { e ->
        logger.error("request exception", e)
    }

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
        logger.info("run middleware list")
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
