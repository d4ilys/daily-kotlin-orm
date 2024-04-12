package com.daily.orm.core.provider.condition

import com.daily.orm.core.provider.select.model.QueryConditionInfo
import com.daily.orm.core.provider.select.model.QueryConditionType
import com.daily.orm.core.provider.select.model.QueryTableInfo
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import java.util.*

open class ConditionBase(val commonUtils: CommonUtils) {

    //表别名映射
    lateinit var operateTableInfos: MutableList<QueryTableInfo>

    //查询的表名
    lateinit var operateTableName: String

    //where条件
    var whereString: String = ""

    //获取别名
    fun getAlias(packageName: String): String {
        return operateTableInfos.first { it.classPackage == packageName }.alias
    }

    //Where条件拼接
    fun whereStringSpilt(conditionQueue: Queue<QueryConditionInfo>, alias: Boolean = true): String {
        val sb = StringBuilder()
        conditionQueue.forEach {
            if (it.type == QueryConditionType.Column) {
                sb.append(commonUtils.quoteColumnName(it.value.toString(), if (alias) getAlias(it.packageName) else ""))
                    .append(" ")
            } else {
                sb.append(it.value).append(" ")
            }
        }
        conditionQueue.clear()
        return sb.toString()
    }
}