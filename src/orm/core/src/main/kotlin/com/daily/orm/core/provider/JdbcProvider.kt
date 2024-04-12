package com.daily.orm.core.provider

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.pool.DruidPooledConnection
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.interfaces.IJdbc
import com.daily.orm.core.provider.internal.StaticApp
import java.sql.ResultSet
import java.sql.Statement


open class JdbcProvider(val commonUtils: CommonUtils) : IJdbc {
    init {
        StaticApp.commonUtils = commonUtils
    }

    /**
     * 连接池
     */
    public lateinit var connectionPool: DruidDataSource

    /**
     * 归还连接至连接池
     */
    private fun returnConnection(druidPooledConnection: DruidPooledConnection) {
        druidPooledConnection.close();
    }

    override fun <T> statementExecute(action: (Statement) -> T): T {

        val druidPooledConnection = connectionPool.connection

        val connection = druidPooledConnection.connection

        val createStatement = connection.createStatement()

        val result = action(createStatement)

        returnConnection(druidPooledConnection)

        return result
    }


    /**
     * 执行SQL语句,返回影响是否成功
     */
    override fun execute(sql: String): Boolean {
        return statementExecute {
            commonUtils.internalOrm.aop.beforeExecute.invoke(sql)
            return@statementExecute it.execute(sql)
        }
    }

    /**
     * 执行SQL语句,返回影响行数
     */
    override fun executeUpdate(sql: String): Int {
        return statementExecute {
            commonUtils.internalOrm.aop.beforeExecute.invoke(sql)
            return@statementExecute it.executeUpdate(sql)
        }
    }

    /**
     * 执行查询语句,返回结果集
     */
    override fun executeQuery(sql: String): ResultSet {
        return statementExecute {
            commonUtils.internalOrm.aop.beforeExecute.invoke(sql)
            val resultSet = it.executeQuery(sql)
            return@statementExecute resultSet
        }
    }

}