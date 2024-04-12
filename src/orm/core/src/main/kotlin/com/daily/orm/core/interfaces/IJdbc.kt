package com.daily.orm.core.interfaces

import java.sql.ResultSet
import java.sql.Statement

interface IJdbc {

    fun <T> statementExecute(action: (Statement) -> T): T

    fun execute(sql: String): Boolean

    fun executeUpdate(sql: String): Int

    fun executeQuery(sql: String): ResultSet
}