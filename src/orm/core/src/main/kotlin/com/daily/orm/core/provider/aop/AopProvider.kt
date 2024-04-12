package com.daily.orm.core.provider.aop

import com.daily.orm.core.interfaces.IAop

class AopProvider : IAop {

    /**
     * 执行前事件
     */
    override val beforeExecute = MulticastDelegate<String>()
}