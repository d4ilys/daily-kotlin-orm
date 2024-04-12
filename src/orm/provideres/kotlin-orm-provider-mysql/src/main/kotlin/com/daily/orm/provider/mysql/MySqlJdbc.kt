package com.daily.orm.provider.mysql

import com.alibaba.druid.pool.DruidDataSource
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.provider.JdbcProvider

class MySqlJdbc(commonUtils: CommonUtils ,url: String, username: String, password: String, initialSize: Int = 1) : JdbcProvider(commonUtils) {
    init {
        //初始化连接池
        val druidDataSource = DruidDataSource()
        druidDataSource.url = url
        druidDataSource.username = username
        druidDataSource.password = password
        druidDataSource.driverClassName = "com.mysql.cj.jdbc.Driver"
        druidDataSource.initialSize = initialSize
        super.connectionPool = druidDataSource
    }

}