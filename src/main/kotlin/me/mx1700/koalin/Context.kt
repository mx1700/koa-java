package me.mx1700.koalin

class Context(val req: Request, val res: Response) {
    lateinit var next: Next
}
