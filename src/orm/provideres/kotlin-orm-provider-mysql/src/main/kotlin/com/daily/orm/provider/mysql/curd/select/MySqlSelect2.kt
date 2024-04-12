package com.daily.orm.provider.mysql.curd.select

import com.daily.orm.core.provider.select.Select2
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils


class MySqlSelect2<T1, T2>(commonUtils: CommonUtils, databaseUtils: DatabaseUtils) :
    Select2<T1, T2>(commonUtils, databaseUtils) {

    override fun toSql(): String {
        return MySqlSelectCommon.internalToSql(operateTableName, whereString, joinMappings)
    }

}