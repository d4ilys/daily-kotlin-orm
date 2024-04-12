package org.example

import com.daily.orm.core.DbType
import com.daily.orm.core.RainbowSql
import com.daily.orm.core.infrastructure.annotations.Column
import com.daily.orm.core.infrastructure.annotations.Table
import com.daily.orm.core.interfaces.IRainbowSql
import com.daily.orm.core.provider.extension.and
import com.daily.orm.core.provider.extension.contains
import com.daily.orm.core.provider.extension.eq
import com.daily.orm.core.provider.extension.update
import com.daily.orm.core.provider.select.*
import com.daily.orm.core.provider.set
import com.daily.orm.core.provider.setSource
import com.daily.orm.core.provider.where

val orm: IRainbowSql = RainbowSql.builder()
    .useDbType(DbType.MySql)
    .useSource("192.168.1.123:3306", "erp_multimedia", "root", "LemiMysql@123.")
    .build();

fun main() {
    testCodeFirst()
}

fun testSelect() {
    var count = 0L;
    val list = orm.from1<User>().select()
        .count { count = it }
        .toList()

    //多表查询
    val sql = orm.from2<User, City>().select()
        .leftJoin { t1::id eq t2::cityId }
        .where { t1::id.eq(123) and t2::name.contains("京") }
        .count { count = it }
        .toSql()
}

fun testUpdate() {

    val res = orm.update<User>()
        .set { this::name update "李四" }
        .set { this::cityId update 3 }
        .where { this::id eq 5 }
        .where { this::name contains "张" }
        .executeAffectedRows()
    println("影响行数$res")
}

fun testDelete() {
    //删除
    orm.delete<User>().where { this::id.eq(2) }.executeAffectedRows();
}

fun testInsert() {
    //插入数据
    orm.insert<User>().setSource(User().apply {
        name = "张三"
        age = 18
        cityId = 1
    }).executeAffectedRows()
}

fun testCodeFirst() {
    orm.codeFirst.syncStructure(User::class)
    orm.codeFirst.syncStructure(City::class)
}

@Table(name = "t_user")
class User {
    @Column(isPrimary = true, isIdentity = true, name = "id", position = 0)
    var id: Int = 0

    @Column(name = "name", position = 1)
    var name: String = ""

    @Column(name = "age", position = 2)
    var age: Int = 0

    @Column(name = "city_id", position = 3)
    var cityId: Int = 0
}


@Table(name = "t_city")
class City {

    @Column(isPrimary = true, isIdentity = true, name = "id", position = 1)
    var id: Int = 0

    @Column(name = "city_id", position = 2)
    var cityId: Int = 0

    @Column(name = "name", position = 3)
    var name: String = ""
}

class UserInfo {
    var id: Int = 0
    var name: String = ""
    var age: Int = 0
    var cityName: String = ""
}

