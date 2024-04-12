package com.daily.orm.core.provider.select

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.condition.ConditionBase
import com.daily.orm.core.provider.select.enums.JoinType
import com.daily.orm.core.provider.select.model.AssignmentExpression
import com.daily.orm.core.provider.select.model.QueryConditionInfo
import com.daily.orm.core.provider.select.model.QueryTableInfo
import com.daily.orm.core.provider.select.tuple.HzyTuple2
import java.util.*

abstract class Select2<T1, T2>(commonUtils: CommonUtils, val databaseUtils: DatabaseUtils) :
    ConditionBase(commonUtils) {

    //连接映射
    val joinMappings: MutableList<AssignmentExpression> = mutableListOf()

    fun any(): Boolean {
        TODO("Not yet implemented")
    }

    abstract fun toSql(): String
}

inline fun <reified T1 : Any, reified T2 : Any> Select2<T1, T2>.select(): Select2<T1, T2> {
    //实例化T1对象，调用where方法，将where条件赋值给where属性
    val table1 = databaseUtils.getTableInfo(T1::class)
    val table2 = databaseUtils.getTableInfo(T2::class)
    val tableAlias1 = "a"
    val tableAlias2 = "b"

    operateTableName = commonUtils.quoteTableName(table1.name, tableAlias1)

    operateTableInfos = mutableListOf(QueryTableInfo().apply {
        tableName = table1.name
        classPackage = table1.classInfo.qualifiedName!!
        alias = tableAlias1

    }, QueryTableInfo().apply {
        tableName = table2.name
        classPackage = table2.classInfo.qualifiedName!!
        alias = tableAlias2
    })

    return this

}

//左连接
inline fun <reified T1 : Any, reified T2 : Any> Select2<T1, T2>.leftJoin(on: HzyTuple2<T1, T2>.() -> AssignmentExpression): Select2<T1, T2> {
    val joinMapping = on(HzyTuple2.createHzyTuple())
    joinMapping.joinType = JoinType.LeftJoin
    joinMapping.leftTableAlias = getAlias(joinMapping.leftTable)
    joinMapping.rightTableAlias = getAlias(joinMapping.rightTable)
    val leftTableInfo = databaseUtils.getTableInfo(joinMapping.leftTable)
    val leftColumnInfo = leftTableInfo.columns.first { it.propertyInfo.name == joinMapping.leftColumn }
    val rightTableInfo = databaseUtils.getTableInfo(joinMapping.rightTable)
    val rightColumnInfo = rightTableInfo.columns.first { it.propertyInfo.name == joinMapping.rightColumn }
    joinMapping.leftTable = commonUtils.quoteTableName(leftTableInfo.name, joinMapping.leftTableAlias)
    joinMapping.leftColumn = commonUtils.quoteColumnName(leftColumnInfo.name, joinMapping.leftTableAlias)
    joinMapping.rightTable = commonUtils.quoteTableName(rightTableInfo.name, joinMapping.rightTableAlias)
    joinMapping.rightColumn = commonUtils.quoteColumnName(rightColumnInfo.name, joinMapping.rightTableAlias)
    //实例化T1对象，调用where方法，将where条件赋值给where属性
    joinMappings.add(joinMapping)
    return this
}

inline fun <reified T1, reified T2> Select2<T1, T2>.where(condition: HzyTuple2<T1, T2>.() -> Queue<QueryConditionInfo>): Select2<T1, T2> {
    //实例化T1对象，调用where方法，将where条件赋值给where属性
    val conditionQueue = condition(HzyTuple2.createHzyTuple())
    whereString = whereStringSpilt(conditionQueue)
    conditionQueue.clear()
    return this
}

inline fun <reified T1, reified T2> Select2<T1, T2>.count(countAction: (Long) -> Unit): Select2<T1, T2> {
    val count = 100L
    countAction(count)
    return this
}

inline fun <reified T1, reified T2, reified TResult> Select2<T1, T2>.toList(): MutableList<TResult> {
    TODO("Not yet implemented")
}

