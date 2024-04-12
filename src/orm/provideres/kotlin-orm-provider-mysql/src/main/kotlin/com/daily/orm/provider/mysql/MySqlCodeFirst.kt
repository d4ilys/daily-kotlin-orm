package com.daily.orm.provider.mysql

import com.daily.orm.core.infrastructure.info.TableInfo
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.provider.CodeFirstProvider
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

data class DDLWarp(val ddl: StringBuilder = StringBuilder())

class MySqlCodeFirst(commonUtils: CommonUtils, databaseUtils: DatabaseUtils) :

    CodeFirstProvider(commonUtils, databaseUtils) {


    override fun <T : Any> syncStructure(type: KClass<T>) {
        val ddlWarp = DDLWarp()
        createTable(ddlWarp, type)
        val ddl = ddlWarp.ddl.toString()
        commonUtils.internalOrm.jdbc.execute(ddl)
    }

    //创建表
    private fun <T : Any> createTable(ddlWarp: DDLWarp, type: KClass<T>) {
        val tableInfo = databaseUtils.getTableInfo(type);
        handleTable(ddlWarp, tableInfo)
    }


    //得到表信息
    private fun handleTable(ddlWarp: DDLWarp, tableInfo: TableInfo) {
        ddlWarp.ddl.append("CREATE TABLE IF NOT EXISTS ")
        ddlWarp.ddl.append(commonUtils.quoteTableName(tableInfo.name))
        handleColumn(ddlWarp, tableInfo)
        ddlWarp.ddl.append(" Engine=InnoDB CHARACTER SET utf8;")
    }

    //得到表结构
    private fun handleColumn(ddlWarp: DDLWarp, tableInfo: TableInfo) {

        ddlWarp.ddl.appendLine(" (")
        //列
        tableInfo.columns.forEach {

            //处理String长度
            if (it.propertyInfo == typeOf<String>() || it.propertyInfo == typeOf<String?>()) {
                it.dbType = it.dbType.replace("255", it.stringLength.toString())
            }

            ddlWarp.ddl.append("${commonUtils.quoteColumnName(it.name)} ${it.dbType}")
            //自增
            if (it.isAutoIncrement) {
                ddlWarp.ddl.append(" AUTO_INCREMENT")
            }
            ddlWarp.ddl.appendLine(",")
        }
        //主键
        tableInfo.primaryKeys.map { it.name }.let {
            ddlWarp.ddl.append(" PRIMARY KEY (")
            it.forEach { p ->
                ddlWarp.ddl.append("${commonUtils.quoteColumnName(p)},")
            }
            ddlWarp.ddl.deleteCharAt(ddlWarp.ddl.length - 1)
            ddlWarp.ddl.appendLine(")")
        }

        ddlWarp.ddl.append(")")
    }
}