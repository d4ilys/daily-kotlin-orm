package com.daily.orm.provider.mysql.curd

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.InsertProvider

class MySqlInsert<T>(commonUtils: CommonUtils, databaseUtils: DatabaseUtils) :
    InsertProvider<T>(commonUtils, databaseUtils) {

    override fun executeAffectedRows(): Int {
        return commonUtils.internalOrm.jdbc.executeUpdate(toSql())
    }

    override fun toSql(): String {
        val sb = StringBuilder()
        sb.append("INSERT INTO ")
        sb.append(insertTableName)
        sb.append("(")
        sb.append(insertColumns)
        sb.append(") VALUES(")
        sb.append(insertValues)
        sb.append(")")
        return sb.toString()
    }
}