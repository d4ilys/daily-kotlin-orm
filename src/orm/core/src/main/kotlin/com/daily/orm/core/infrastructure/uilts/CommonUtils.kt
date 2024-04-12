package com.daily.orm.core.infrastructure.uilts

import com.daily.orm.core.interfaces.IRainbowSql

abstract class CommonUtils(val internalOrm: IRainbowSql) {

    abstract fun quoteColumnName(name: String, tableAlias: String = ""): String

    abstract fun quoteTableName(name: String, tableAlias: String = ""): String

    abstract fun quoteValue(value: Any?): String

    open fun startWithHandler(value: String): String {

        return "'%$value'"
    }

    open fun endWithHandler(value: String): String {
        return "'$value%'"
    }

    open fun containHandler(value: String): String {
        return "'%$value%'"
    }
}