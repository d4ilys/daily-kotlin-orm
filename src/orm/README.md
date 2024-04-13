# Kotlin-ORM

实验性项目

> 创建ORM对象

~~~kotlin

//必须是单例的，内部存在连接池
val orm: IRainbowSql = RainbowSql.builder()
    .useDbType(DbType.MySql)
    .useSource("192.168.1.123:3306", "table", "root", "123")
    .build();

~~~

> 支持CodeFirst

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

//生成表
orm.codeFirst.syncStructure(User::class)
orm.codeFirst.syncStructure(City::class)

//SQL
CREATE TABLE IF NOT EXISTS `t_user` (
`id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(255) NOT NULL,
`age` INT NOT NULL,
`city_id` INT NOT NULL,
 PRIMARY KEY (`id`)
) Engine=InnoDB CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS `t_city` (
`id` INT NOT NULL AUTO_INCREMENT,
`city_id` INT NOT NULL,
`name` VARCHAR(255) NOT NULL,
 PRIMARY KEY (`id`)
) Engine=InnoDB CHARACTER SET utf8;

~~~

