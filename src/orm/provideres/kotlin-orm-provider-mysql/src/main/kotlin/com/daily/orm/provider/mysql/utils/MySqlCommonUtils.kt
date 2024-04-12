package com.daily.orm.provider.mysql.utils

import com.daily.orm.core.RainbowSql
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.interfaces.IRainbowSql
import java.time.LocalDateTime

class MySqlCommonUtils(rainbowSql: IRainbowSql) : CommonUtils(rainbowSql) {

    override fun quoteColumnName(name: String, tableAlias: String): String {
        return if (tableAlias.isNotEmpty()) "$tableAlias.`$name`" else "`$name`"

    }

    override fun quoteTableName(name: String, tableAlias: String): String {
        return if (tableAlias.isNotEmpty()) "`$name` $tableAlias" else "`$name`"
    }

    override fun quoteValue(value: Any?): String {
        when (value) {
            is String -> {
                return "'$value'"
            }

            is LocalDateTime -> {
                return "'$value'"
            }

            is Boolean -> {
                return if (value) "1" else "0"
            }

            is Int -> {
                return value.toString()
            }

            else -> return value.toString()
        }
    }
}