package com.daily.orm.core.infrastructure.annotations


@Target(AnnotationTarget.PROPERTY)
annotation class Column(

    val name: String = "",

    val stringLength: Int = 255,

    val isNullable: Boolean = true,

    val isIdentity: Boolean = false,

    val position: Int = 0,

    val isPrimary: Boolean = false,

    val dbType: String = ""
)