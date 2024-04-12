package com.daily.orm.core.provider

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.condition.ConditionBase
import com.daily.orm.core.provider.select.Select1
import com.daily.orm.core.provider.select.model.QueryConditionInfo
import java.util.*

abstract class DeleteProvider<T>(commonUtils: CommonUtils, val databaseUtils: DatabaseUtils) :
    ConditionBase(commonUtils) {

    /**
     * 返回受影响的行数
     */
    abstract fun executeAffectedRows(): Int

    /**
     * 转换为sql
     */
    abstract fun toSql(): String

}

inline fun <reified T1 : Any> DeleteProvider<T1>.where(condition: T1.() -> Queue<QueryConditionInfo>): DeleteProvider<T1> {
    //实例化T1对象，调用where方法，将where条件赋值给where属性
    val conditionQueue = condition(T1::class.java.getDeclaredConstructor().newInstance())
    operateTableName = commonUtils.quoteTableName(databaseUtils.getTableInfo(T1::class).name)
    whereString = whereStringSpilt(conditionQueue, false)
    conditionQueue.clear()
    return this
}