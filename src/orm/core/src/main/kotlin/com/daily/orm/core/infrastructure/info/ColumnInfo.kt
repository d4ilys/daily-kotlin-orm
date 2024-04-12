package com.daily.orm.core.infrastructure.info

import kotlin.reflect.KProperty
import kotlin.reflect.KType

class ColumnInfo {

    lateinit var propertyInfo: KProperty<*>

    var name: String = ""

    var dbType: String = ""

    var isPrimaryKey: Boolean = false

    var isAutoIncrement: Boolean = false

    var position: Int = 0

    var stringLength: Int = 0

}