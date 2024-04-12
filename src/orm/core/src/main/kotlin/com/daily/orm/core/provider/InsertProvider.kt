package com.daily.orm.core.provider

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import kotlin.reflect.full.memberProperties

abstract class InsertProvider<T>(val commonUtils: CommonUtils, val databaseUtils: DatabaseUtils) {

    lateinit var insertTableName: String

    lateinit var insertColumns: String

    lateinit var insertValues: String

    /**
     * 返回受影响的行数
     */
    abstract fun executeAffectedRows(): Int

    /**
     * 转换为sql
     */
    abstract fun toSql(): String
}

inline fun <reified T : Any> InsertProvider<T>.setSource(entity: T): InsertProvider<T> {
    val kClass = T::class
    val tableInfo = databaseUtils.getTableInfo(kClass)
    insertTableName = commonUtils.quoteTableName(tableInfo.name)
    //组装列
    val columns = tableInfo.columns.map {
        commonUtils.quoteColumnName(it.name)
    }
    insertColumns = columns.joinToString(",");
    //组装值
    val memberProperties = kClass.memberProperties
    val sb = StringBuilder();
    tableInfo.columns.forEach {
        val property = memberProperties.first { t -> t == it.propertyInfo }
        val value = property.get(entity)
        sb.append(commonUtils.quoteValue(value)).append(",")
    }
    //去掉最后一个逗号
    sb.deleteCharAt(sb.length - 1)
    insertValues = sb.toString()
    return this
}