package com.daily.orm.provider.mysql.curd

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.UpdateProvider
import com.daily.orm.provider.mysql.utils.MySqlDatabaseUtils

class MySqlUpdate<T>(commonUtils: CommonUtils, databaseUtils: DatabaseUtils) :
    UpdateProvider<T>(commonUtils, databaseUtils) {

    override fun executeAffectedRows(): Int {
        val sql = toSql();
        if (whereString.isEmpty()) return 0
        return commonUtils.internalOrm.jdbc.executeUpdate(sql)
    }

    override fun toSql(): String {
        if (operateTableName.isEmpty()) return ""
        val sb = StringBuilder()
        sb.append("UPDATE ")
        sb.append(operateTableName)
        sb.append(" SET ")
        sets.forEach {
            sb.append(it.first)
            sb.append(" = ")
            sb.append(it.second)
            sb.append(",")
        }
        //去掉最后一个逗号
        sb.deleteCharAt(sb.length - 1)
        sb.appendLine()
        sb.append("WHERE ")
        sb.append(whereString)
        return sb.toString()
    }

}