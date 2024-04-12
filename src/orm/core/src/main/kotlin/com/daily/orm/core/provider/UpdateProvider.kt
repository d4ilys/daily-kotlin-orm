package com.daily.orm.core.provider

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.condition.ConditionBase
import com.daily.orm.core.provider.extension.getPackName
import com.daily.orm.core.provider.select.model.QueryConditionInfo
import java.util.*
import kotlin.reflect.KProperty

abstract class UpdateProvider<T>(commonUtils: CommonUtils, val databaseUtils: DatabaseUtils) :
    ConditionBase(commonUtils) {

    val sets: MutableList<Pair<String, Any>> = mutableListOf()

    /**
     * 返回受影响的行数
     */
    abstract fun executeAffectedRows(): Int

    /**
     * 转换为sql
     */
    abstract fun toSql(): String

}


inline fun <reified T1 : Any> UpdateProvider<T1>.where(condition: T1.() -> Queue<QueryConditionInfo>): UpdateProvider<T1> {
    //实例化T1对象，调用where方法，将where条件赋值给where属性
    val conditionQueue = condition(T1::class.java.getDeclaredConstructor().newInstance())
    operateTableName = commonUtils.quoteTableName(databaseUtils.getTableInfo(T1::class).name)
    if (whereString.isEmpty()) {
        whereString = whereStringSpilt(conditionQueue, false)
    } else {
        whereString += "and " + whereStringSpilt(conditionQueue, false)
    }
    conditionQueue.clear()
    return this
}

inline fun <reified T1 : Any> UpdateProvider<T1>.set(action: T1.() -> Pair<String, Any>): UpdateProvider<T1> {
    val tableInfo = databaseUtils.getTableInfo(T1::class)
    val columns = tableInfo.columns
    val actionSet = action(T1::class.java.getDeclaredConstructor().newInstance())
    val column = columns.first { it.propertyInfo.name == actionSet.first }
    sets.add(Pair(commonUtils.quoteColumnName(column.name), commonUtils.quoteValue(actionSet.second)))
    return this
}
