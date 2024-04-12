package com.daily.orm.provider.mysql.curd.select

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.provider.select.Select1
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils

class MySqlSelect1<T>(commonUtils: CommonUtils, databaseUtils: DatabaseUtils) :
    Select1<T>(commonUtils, databaseUtils) {

    override fun toSql(): String {
        return MySqlSelectCommon.internalToSql(operateTableName, whereString)
    }

}