# daily-kotlin-orm
**前言**

作者平时用C#比较多，写起来比较的爽，但是最近要对接一个分布式文件系统 Minio，发现C#中的驱动太拉跨 提了很久ISSUE也没人处理 只能说C#的生态还是有待进步.. 于是决定用其他语言来对接，本来想用Golang来写 但是调研了一番还是不合适..

说到生态 最好的肯定是JAVA，所以想用JAVA来对接，但是呢想到JAVA那恶心的语法我就难受，尤其是哪个泛型 搞得的我难受至极，而且没有重载运算符、表达式目录树之类的..

反正这个中间件也是我自己在写，于是直接用了Kotlin~

Kotlin 扩展方法、扩展属性 、DSL、Smart Cast、Inline Reified 等等特性 用起来很爽，也兼容JAVA生态 所以开干。

于是很快的对接好了Minio，这时候需要操作数据库，于是看了一下Ktorm，用了一下 感觉很别扭，因为C#写的多了，感觉Codefirst已经是基操了，但是Ktorm中没有... 

我本人是.Net生态中FreeSql的贡献者，对ORM的原理略知一二，于是一不做二不休 打算写一个简单的 自己用.. 

**有感而发**

虽然Kotlin的语法非常的优美，在某些方面甚至比C#还好用，比如**扩展属性**、**Smart Cast** 但是呢受限于JVM，对泛型的支持还是太差。

inline reified 可以获取泛型的真实类型，但是要求类中相关的成员变量必须开放，这就造成了污染 并且想要获取JAVA中的泛型类型就非常难，例如 T 的类型为 List<String> ，想要获取List中的String得费点功夫。

所以C#依然无敌，真泛型 表达式目录树 牛逼的反射 Unsafe ....

**下一步计划**

这个ORM只能支持基本的CRUD但是架构设计 扩展性挺好，随时可以扩展新的功能,但是这个中间件写完以后没有了需求后可能就会搁置了..

而且极致性能待优化 例如 在ResultSet转List时可以使用 ASM 动态构建函数式接口 并缓存，类似于 C# 中使用EMIT/ExpressionTree 动态构建委托，  并缓存

如果关注的人比较少，也会搁置，因为第一考虑的语言还是C# .... 

本项目可以为大家提供一个思路.. 来自C# 优雅语法的ORM思路...

> 支持CodeFirst

### ORM对象构建

~~~kotlin

//一定要是单例的
val orm: IRainbowSql = RainbowSql.builder()
    .useDbType(DbType.MySql)
    .useSource("192.168.1.123:3306", "erp_multimedia", "root", "123.")
    .build();

~~~

### 实体模型

~~~kotlin

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

~~~

### CodeFirst生成数据表

~~~kotlin

fun testCodeFirst() {
    orm.codeFirst.syncStructure(User::class)
    orm.codeFirst.syncStructure(City::class)
}

~~~

~~~sql

执行SQL :
======================================================================================================
CREATE TABLE IF NOT EXISTS `t_user` (
`id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(255) NOT NULL,
`age` INT NOT NULL,
`city_id` INT NOT NULL,
 PRIMARY KEY (`id`)
) Engine=InnoDB CHARACTER SET utf8;
======================================================================================================

执行SQL :
======================================================================================================
CREATE TABLE IF NOT EXISTS `t_city` (
`id` INT NOT NULL AUTO_INCREMENT,
`city_id` INT NOT NULL,
`name` VARCHAR(255) NOT NULL,
 PRIMARY KEY (`id`)
) Engine=InnoDB CHARACTER SET utf8;
======================================================================================================

~~~

### 查询

~~~kotlin

    var count = 0L;
    //多表查询
    val sql = orm.from2<User, City>().select()
        .leftJoin { t1::id eq t2::cityId }
        .where { t1::id.eq(123) and t2::name.contains("京") }
        .count { count = it }
        .toList()

~~~

~~~sql

SELECT * FROM `t_user` a 
LEFT JOIN `t_city` b ON a.`id` = b.`city_id`
WHERE a.`id` = 123 and b.`name` like '%京%' 

~~~

### 新增

~~~kotlin

//插入数据
orm.insert<User>().setSource(User().apply {
    name = "张三"
    age = 18
    cityId = 1
}).executeAffectedRows()

//INSERT INTO `t_user`(`id`,`name`,`age`,`city_id`) VALUES(0,'张三',18,1)

~~~

### 修改

~~~kotlin

 //修改
 val res = orm.update<User>()
     .set { this::name update "李四" }
     .set { this::cityId update 3 }
     .where { this::id eq 5 }
     .where { this::name contains "张" }
     .executeAffectedRows()

//UPDATE `t_user` SET `name` = '李四',`city_id` = 3
//WHERE `id` = 5 and `name` like '%张%' 

~~~

### 删除

~~~kotlin

 //删除
orm.delete<User>().where { this::id.eq(2) }.executeAffectedRows();

//DELETE FROM `t_user` WHERE `id` = 2 

~~~

