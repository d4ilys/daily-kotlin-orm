package com.daily.orm.core.infrastructure.uilts

import com.daily.orm.core.infrastructure.info.TableInfo
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class CacheUtils {

    companion object {
        // cache for TableInfo
        internal val tableInfoCache: ConcurrentMap<String, TableInfo> by lazy {
            val concurrentHashMap = ConcurrentHashMap<String, TableInfo>()
            concurrentHashMap
        }

    }

}