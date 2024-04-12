package com.daily.orm.core.infrastructure.annotations

@Target(AnnotationTarget.CLASS)
annotation class Table(
    val name: String
)