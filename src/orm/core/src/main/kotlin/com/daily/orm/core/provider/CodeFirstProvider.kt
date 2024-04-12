package com.daily.orm.core.provider

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.interfaces.ICodeFirst
import com.daily.orm.core.interfaces.IRainbowSql
import kotlin.reflect.KClass

abstract class CodeFirstProvider(val commonUtils: CommonUtils, val databaseUtils: DatabaseUtils) :
    ICodeFirst {
    abstract override fun <T : Any> syncStructure(type: KClass<T>)
}