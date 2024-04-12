package com.daily.orm.core.interfaces

import com.daily.orm.core.provider.DeleteProvider
import com.daily.orm.core.provider.select.Select1
import com.daily.orm.core.provider.UpdateProvider
import com.daily.orm.core.provider.InsertProvider
import com.daily.orm.core.provider.select.Select2


interface IRainbowSql {

    var jdbc: IJdbc

    val codeFirst: ICodeFirst

    val aop: IAop

    fun <T1> from1(): Select1<T1>

    fun <T1, T2> from2(): Select2<T1, T2>

    fun <T1> update(): UpdateProvider<T1>

    fun <T1> delete(): DeleteProvider<T1>

    fun <T1> insert(): InsertProvider<T1>
}

