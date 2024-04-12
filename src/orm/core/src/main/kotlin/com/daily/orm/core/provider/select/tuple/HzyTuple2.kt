package com.daily.orm.core.provider.select.tuple

class HzyTuple2<T1, T2>(var t1: T1, var t2: T2) {
    companion object {
        inline fun <reified T1, reified T2> createHzyTuple(): HzyTuple2<T1, T2> {
            val first = T1::class.java.getDeclaredConstructor().newInstance()
            val second = T2::class.java.getDeclaredConstructor().newInstance()
            return HzyTuple2(first, second)
        }
    }
}