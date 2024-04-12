package com.daily.orm.provider.mysql.curd

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.DeleteProvider


class MySqlDelete<T>(commonUtils: CommonUtils, databaseUtils: DatabaseUtils) :
    DeleteProvider<T>(commonUtils, databaseUtils) {
    override fun executeAffectedRows(): Int {
        val sql = toSql();
        if (whereString.isEmpty()) return 0
        return commonUtils.internalOrm.jdbc.executeUpdate(sql)
    }

    override fun toSql(): String {
        if (operateTableName.isEmpty()) return ""
        val sb = StringBuilder()
        sb.append("DELETE FROM ")
        sb.append(operateTableName)
        sb.append(" WHERE ")
        sb.append(whereString)
        return sb.toString()
    }
}