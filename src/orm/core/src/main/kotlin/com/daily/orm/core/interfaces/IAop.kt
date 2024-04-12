package com.daily.orm.core.interfaces

import com.daily.orm.core.provider.aop.MulticastDelegate

interface IAop {
    val beforeExecute: MulticastDelegate<String>
}