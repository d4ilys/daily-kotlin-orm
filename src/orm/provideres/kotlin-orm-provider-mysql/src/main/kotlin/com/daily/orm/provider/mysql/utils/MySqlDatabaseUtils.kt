package com.daily.orm.provider.mysql.utils

import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class MySqlDatabaseUtils: DatabaseUtils() {

    override val mappings: MutableMap<KType, String> by lazy {
        val map = mutableMapOf(
            Pair(typeOf<Int>(), "INT NOT NULL"),
            Pair(typeOf<Int?>(), "INT"),
            Pair(typeOf<Long>(), "BIGINT NOT NULL"),
            Pair(typeOf<Long?>(), "BIGINT"),
            Pair(typeOf<String>(), "VARCHAR(255) NOT NULL"),
            Pair(typeOf<String?>(), "VARCHAR(255)"),
            Pair(typeOf<LocalDateTime>(), "DATETIME(3) NOT NULL"),
            Pair(typeOf<LocalDateTime?>(), "DATETIME(3)"),
            Pair(typeOf<Boolean>(), "BOOLEAN NOT NULL"),
            Pair(typeOf<Boolean?>(), "BOOLEAN"),
            Pair(typeOf<Double>(), "DOUBLE  NOT NULL"),
            Pair(typeOf<Double?>(), "DOUBLE"),
            Pair(typeOf<Float>(), "FLOAT NOT NULL"),
            Pair(typeOf<Float?>(), "FLOAT"),
            Pair(typeOf<ByteArray>(), "BYTEA NOT NULL"),
            Pair(typeOf<ByteArray?>(), "BYTEA"),
            Pair(typeOf<Any>(), "TEXT NOT NULL"),
            Pair(typeOf<Any?>(), "TEXT"),
            Pair(typeOf<BigDecimal>(), "DECIMAL(10,2) NOT NULL"),
            Pair(typeOf<BigDecimal?>(), "DECIMAL(10,2)"),

            )
        map
    }

}