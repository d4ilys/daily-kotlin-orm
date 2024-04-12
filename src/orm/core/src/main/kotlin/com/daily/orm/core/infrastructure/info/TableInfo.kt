package com.daily.orm.core.infrastructure.info

import kotlin.reflect.KClass

class TableInfo {

    var name: String = ""

    lateinit var classInfo: KClass<*>

    var columns: MutableList<ColumnInfo> = mutableListOf()

    val primaryKeys: MutableList<ColumnInfo> = mutableListOf()

}