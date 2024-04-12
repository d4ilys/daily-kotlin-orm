package com.daily.orm.core.infrastructure.uilts

import com.daily.orm.core.infrastructure.annotations.Column
import com.daily.orm.core.infrastructure.annotations.Table
import com.daily.orm.core.infrastructure.info.ColumnInfo
import com.daily.orm.core.infrastructure.info.TableInfo
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

abstract class DatabaseUtils {

    abstract val mappings: MutableMap<KType, String>

    //获取表信息
    fun <T : Any> getTableInfo(type: KClass<T>): TableInfo {
        return CacheUtils.tableInfoCache.computeIfAbsent(type.qualifiedName!!) {
            val findAnnotation = type.findAnnotation<Table>()
            val tableName = findAnnotation?.name ?: type.simpleName
            val tableInfo = TableInfo()
            //表名
            tableInfo.name = tableName!!

            tableInfo.classInfo = type
            //列信息
            addColumn(tableInfo, type)

            tableInfo.columns.sortBy { it.position }

            tableInfo
        }
    }

    fun  getTableInfo(packName: String): TableInfo {
        val type = Class.forName(packName).kotlin
        return getTableInfo(type)
    }

    //处理列信息
    private fun <T : Any> addColumn(tableInfo: TableInfo, type: KClass<T>) {
        type.declaredMemberProperties.forEach {
            val returnType = it.returnType
            val columnAnnotation = it.findAnnotation<Column>()
            val name = columnAnnotation?.name ?: it.name

            val columnInfo = ColumnInfo()
            columnInfo.name = name
            columnInfo.propertyInfo = it
            columnInfo.isPrimaryKey = columnAnnotation?.isPrimary ?: false
            columnInfo.isAutoIncrement = columnAnnotation?.isIdentity ?: false
            columnInfo.position = columnAnnotation?.position ?: 0
            columnInfo.stringLength = columnAnnotation?.stringLength ?: 0


            if ("" != columnAnnotation?.dbType) {
                if (columnAnnotation != null) columnInfo.dbType = columnAnnotation.dbType
            } else {
                mappings[returnType]?.let { s ->
                    columnInfo.dbType = s
                }
            }

            tableInfo.columns.add(columnInfo)

            if (columnInfo.isPrimaryKey) {
                tableInfo.primaryKeys.add(columnInfo)
            }
        }
    }

}