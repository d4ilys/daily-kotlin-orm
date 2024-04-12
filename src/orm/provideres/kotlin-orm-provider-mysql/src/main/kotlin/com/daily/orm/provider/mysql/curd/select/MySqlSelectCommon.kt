package com.daily.orm.provider.mysql.curd.select

import com.daily.orm.core.provider.select.enums.JoinType
import com.daily.orm.core.provider.select.model.AssignmentExpression

class MySqlSelectCommon {
    companion object {
        fun internalToSql(
            selectTableName: String,
            where: String,
            joinMappings: MutableList<AssignmentExpression>? = null
        ): String {
            val sb = StringBuilder()
            sb.append("SELECT ").append("* ").append("FROM ").append(selectTableName)
            //拼接左连接
            if (joinMappings != null) {
                for (joinMapping in joinMappings) {
                    if (joinMapping.joinType == JoinType.LeftJoin) {
                        sb.appendLine(" ").append("LEFT JOIN ").append(joinMapping.rightTable)
                            .append(" ")
                            .append("ON ")
                            .append(joinMapping.leftColumn)
                            .append(" = ")
                            .append(joinMapping.rightColumn)
                            .appendLine("")
                    }
                }
            }
            if (where.isNotEmpty()) sb.append("WHERE ").append(where)
            return sb.toString()
        }
    }
}