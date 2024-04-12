package com.daily.orm.core

import com.daily.orm.core.interfaces.IRainbowSql
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RainbowSql {

    private lateinit var url: String

    private lateinit var username: String
    private lateinit var password: String
    private var initialSize: Int = 0
    private lateinit var dbType: DbType

    companion object {
        fun builder(): RainbowSql {
            return RainbowSql()
        }
    }

    fun useDbType(dbType: DbType): RainbowSql {
        this.dbType = dbType
        return this
    }

    fun useSource(url: String, username: String, password: String, initialSize: Int = 1): RainbowSql {
        this.url = url
        this.username = username
        this.password = password
        this.initialSize = initialSize
        return this
    }

    fun useSource(
        host: String,
        dbName: String,
        username: String,
        password: String,
        initialSize: Int = 1
    ): RainbowSql {
        this.url = "jdbc:mysql://$host/$dbName"
        this.username = username
        this.password = password
        this.initialSize = initialSize
        return this
    }

    fun build(): IRainbowSql {

        lateinit var orm: IRainbowSql

        when (dbType) {

            DbType.MySql -> {
                val clazz = Class.forName("com.daily.orm.provider.mysql.MySqlProvider")
                val constructor = clazz.constructors.find { it.parameterCount == 4 };
                if (constructor != null) {
                    orm = constructor.newInstance(url, username, password, initialSize) as IRainbowSql
                }
            }

            DbType.Oracle -> {
                val clazz = Class.forName("com.daily.orm.provider.oracle.OracleProvider")
                orm = clazz.constructors[0].newInstance(url, username, password, initialSize) as IRainbowSql
            }

            DbType.SQLite -> {
                val clazz = Class.forName("com.daily.orm.provider.sqlite.SqliteProvider")
                orm = clazz.constructors[0].newInstance(url, username, password, initialSize) as IRainbowSql
            }

            DbType.SqlServer -> {
                val clazz = Class.forName("com.daily.orm.provider.sqlserver.SqlServerProvider")
                orm = clazz.constructors[0].newInstance(url, username, password, initialSize) as IRainbowSql
            }

            else -> {
                throw IllegalArgumentException("Unsupported database type: $dbType")
            }
        }

        orm.aop.beforeExecute += { sql ->
            println(
                """${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } - 执行SQL :
======================================================================================================
$sql
======================================================================================================
"""
            )
        }

        return orm
    }

}