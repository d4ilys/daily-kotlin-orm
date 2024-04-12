package com.daily.orm.core.provider.aop

class MulticastDelegate<T>(private val invocationList: MutableList<(T) -> Unit> = mutableListOf()) {
    operator fun plusAssign(handler: (T) -> Unit) {
        invocationList.add(handler)
    }

    operator fun minusAssign(handler: (T) -> Unit) {
        invocationList.remove(handler)
    }

    operator fun invoke(value: T) {
        for (handler in invocationList) {
            handler(value)
        }
    }
}