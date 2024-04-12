package com.daily.orm.core.provider

import com.daily.orm.core.interfaces.IRainbowSql
import com.daily.orm.core.provider.select.Select1
import com.daily.orm.core.provider.select.Select2
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.interfaces.IAop
import com.daily.orm.core.provider.aop.AopProvider

abstract class BaseDbProvider : IRainbowSql {

    abstract val internalCommonUtils: CommonUtils

    abstract val internalDatabaseUtils: DatabaseUtils

    override val aop: IAop = AopProvider()

    abstract fun <T1> createSelect1(): Select1<T1>

    abstract fun <T1, T2> createSelect2(): Select2<T1, T2>

    abstract fun <T> createUpdate(): UpdateProvider<T>

    abstract fun <T> createDelete(): DeleteProvider<T>

    abstract fun <T> createInsert(): InsertProvider<T>

    override fun <T> update(): UpdateProvider<T> {
        return createUpdate()
    }

    override fun <T> delete(): DeleteProvider<T> {
        return createDelete<T>()
    }

    override fun <T> insert(): InsertProvider<T> {
        return createInsert()
    }

    override fun <T1> from1(): Select1<T1> {
        return createSelect1()
    }

    override fun <T1, T2> from2(): Select2<T1, T2> {
        return createSelect2()
    }
}