package mx1700.koa

import me.mx1700.Context
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

typealias Next = () -> Unit
typealias Middleware = (Context, Next) -> Unit
typealias OnException = Context.(Exception) -> Unit

class Koa {

    val callback = this::run
    private val middlewareList = arrayListOf<Middleware>()
    private var onExceptionAction : OnException? = null

    /**
     * 使用中间件
     */
    fun use(action: Context.(Next) -> Unit) {
        middlewareList.add(action)
    }

    /**
     * 开始执行中间件
     */
    private fun run(request: HttpServletRequest,
                    response: HttpServletResponse) {
        val ctx = Context(request, response)
        next(0, ctx)
    }

    /**
     * 执行下一个中间件
     */
    private fun next(index: Int, ctx: Context) {
        if (index >= middlewareList.count()) return;
        val middleware = middlewareList[index]
        try {
            middleware(ctx, { next(index + 1, ctx) })
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
    fun onException(action: Context.(Exception) -> Unit) {
        onExceptionAction = action
    }
}
