package com.daily.orm.core.interfaces

import kotlin.reflect.KClass

interface ICodeFirst {

    //同步表
    fun <T : Any> syncStructure(type: KClass<T>);

}