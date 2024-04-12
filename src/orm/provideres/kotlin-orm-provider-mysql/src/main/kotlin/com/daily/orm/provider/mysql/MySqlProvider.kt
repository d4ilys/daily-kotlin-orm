package com.daily.orm.provider.mysql

import com.daily.orm.core.provider.BaseDbProvider
import com.daily.orm.core.provider.UpdateProvider
import com.daily.orm.core.provider.select.Select1
import com.daily.orm.core.provider.select.Select2
import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils
import com.daily.orm.core.interfaces.ICodeFirst
import com.daily.orm.core.interfaces.IJdbc
import com.daily.orm.core.provider.DeleteProvider
import com.daily.orm.core.provider.InsertProvider
import com.daily.orm.core.provider.internal.StaticApp
import com.daily.orm.provider.mysql.curd.MySqlDelete
import com.daily.orm.provider.mysql.curd.MySqlInsert
import com.daily.orm.provider.mysql.curd.MySqlUpdate
import com.daily.orm.provider.mysql.curd.select.MySqlSelect1
import com.daily.orm.provider.mysql.curd.select.MySqlSelect2
import com.daily.orm.provider.mysql.utils.MySqlCommonUtils
import com.daily.orm.provider.mysql.utils.MySqlDatabaseUtils

class MySqlProvider(url: String, username: String, password: String, initialSize: Int = 1) : BaseDbProvider() {

    override val internalCommonUtils: CommonUtils = MySqlCommonUtils(this)

    override val internalDatabaseUtils: DatabaseUtils = MySqlDatabaseUtils()

    override val codeFirst: ICodeFirst = MySqlCodeFirst(this.internalCommonUtils, this.internalDatabaseUtils)

    override var jdbc: IJdbc = MySqlJdbc(internalCommonUtils, url, username, password, initialSize)

    override fun <T1> createSelect1(): Select1<T1> {
        return MySqlSelect1(this.internalCommonUtils, this.internalDatabaseUtils)
    }

    override fun <T1, T2> createSelect2(): Select2<T1, T2> {
        return MySqlSelect2(this.internalCommonUtils, this.internalDatabaseUtils)
    }

    override fun <T> createInsert(): InsertProvider<T> {
        return MySqlInsert(this.internalCommonUtils, this.internalDatabaseUtils)
    }

    override fun <T> createUpdate(): UpdateProvider<T> {
        return MySqlUpdate(this.internalCommonUtils, this.internalDatabaseUtils)
    }

    override fun <T> createDelete(): DeleteProvider<T> {
        return MySqlDelete(this.internalCommonUtils, this.internalDatabaseUtils)
    }
}