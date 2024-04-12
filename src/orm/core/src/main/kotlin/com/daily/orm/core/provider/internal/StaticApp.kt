package com.daily.orm.core.provider.internal

import com.daily.orm.core.infrastructure.uilts.CommonUtils
import com.daily.orm.core.infrastructure.uilts.DatabaseUtils

class StaticApp {
    companion object {
        lateinit var commonUtils: CommonUtils
        lateinit var databaseUtils: DatabaseUtils
    }
}