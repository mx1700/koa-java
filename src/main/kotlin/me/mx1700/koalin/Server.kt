package me.mx1700.koalin

import java.lang.Exception

typealias Next = suspend () -> Unit
typealias Middleware = suspend (Context, Next) -> Unit
typealias OnException = suspend Context.(Exception) -> Unit

class Server {

    val callback: suspend (Request, Response) -> Unit = { req, res -> this.run(req, res) }

    private val middlewareList = arrayListOf<Middleware>()
    private var onExceptionAction : OnException? = null

    /**
     * 使用中间件
     */
    fun use(action: suspend Context.(Next) -> Unit) {
        middlewareList.add(action)
    }

    /**
     * 开始执行中间件
     */
    suspend private fun run(request: Request,
                    response: Response) {
        val ctx = Context(request, response)
        next(0, ctx)
    }

    /**
     * 执行下一个中间件
     */
    suspend private fun next(index: Int, ctx: Context) {
        if (index >= middlewareList.count()) return;
        val middleware = middlewareList[index]
        try {
            middleware(ctx, {
                next(index + 1, ctx)
            })
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
    fun onException(action: suspend Context.(Exception) -> Unit) {
        onExceptionAction = action
    }
}
