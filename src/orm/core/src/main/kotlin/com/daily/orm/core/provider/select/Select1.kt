package com.daily.orm.core.provider.select

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.condition.ConditionBase
import com.daily.orm.core.provider.select.model.QueryConditionInfo
import com.daily.orm.core.provider.select.model.QueryTableInfo
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberExtensionProperties
import kotlin.reflect.full.memberProperties

abstract class Select1<T1>(commonUtils: CommonUtils, val databaseUtils: DatabaseUtils) : ConditionBase(commonUtils) {

    fun any(): Boolean {
        TODO("Not yet implemented")
    }

    abstract fun toSql(): String
}

inline fun <reified T1 : Any> Select1<T1>.select(): Select1<T1> {
    //实例化T1对象，调用where方法，将where条件赋值给where属性
    val table1 = databaseUtils.getTableInfo(T1::class)
    val tableAlias1 = "a"
    operateTableName = commonUtils.quoteTableName(table1.name, tableAlias1)
    operateTableInfos = mutableListOf(QueryTableInfo().apply {
        tableName = table1.name
        classPackage = table1.classInfo.qualifiedName!!
        alias = tableAlias1
    })
    return this

}


inline fun <reified T1> Select1<T1>.where(condition: T1.() -> Queue<QueryConditionInfo>): Select1<T1> {
    //实例化T1对象，调用where方法，将where条件赋值给where属性
    val conditionQueue = condition(T1::class.java.getDeclaredConstructor().newInstance())
    whereString = whereStringSpilt(conditionQueue)
    conditionQueue.clear()
    return this
}

inline fun <reified T1> Select1<T1>.count(countAction: (Long) -> Unit): Select1<T1> {
    val count = 100L
    countAction(count)
    return this
}

inline fun <reified T1 : Any> Select1<T1>.toList(): MutableList<T1> {
    val sql = toSql()
    val resultSet = commonUtils.internalOrm.jdbc.executeQuery(sql)
    val list: MutableList<T1> = mutableListOf()
    val columns = databaseUtils.getTableInfo(T1::class).columns

    while (resultSet.next()) {
        val type = T1::class
        val instance = type.java.getDeclaredConstructor().newInstance()
        val properties = type.memberProperties

        columns.forEach {
            val property = it.propertyInfo as KMutableProperty1<*, *>
            val value = resultSet.getObject(it.name)
            property.setter.call(instance, value)
            //设置属性值
        }
        list.add(instance as T1)
    }
    return list
}