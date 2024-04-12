package com.daily.orm.core.provider.extension

import com.daily.orm.core.provider.internal.StaticApp
import com.daily.orm.core.provider.select.enums.LikeType
import com.daily.orm.core.provider.select.model.AssignmentExpression
import com.daily.orm.core.provider.select.model.QueryConditionInfo
import com.daily.orm.core.provider.select.model.QueryConditionType
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.javaField


//#region eq extension function

infix fun <T> KProperty0<T>.eq(value: T): Queue<QueryConditionInfo> {
    val name = this.name
    val packageName = this.getPackName();
    return LinkedList<QueryConditionInfo>().apply {
        add(QueryConditionInfo(name, QueryConditionType.Column, packageName))
        add(QueryConditionInfo("=", QueryConditionType.Operator))
        add(QueryConditionInfo(value, QueryConditionType.Value))
    }
}


infix fun <T> KProperty0<T>.eq(right: KProperty0<T>): AssignmentExpression {
    val ass = AssignmentExpression();
    val left = this
    ass.leftTable = left.getPackName()
    ass.rightTable = right.getPackName()
    ass.leftColumn = left.name
    ass.rightColumn = right.name
    return ass
}

//#endregion

infix fun <T> KProperty0<T>.update(value: T): Pair<String, T> {
    return Pair(this.name, value)
}


infix fun Queue<QueryConditionInfo>.and(queryConditionInfo: Queue<QueryConditionInfo>): Queue<QueryConditionInfo> {
    this.add(QueryConditionInfo("and", QueryConditionType.Operator))
    this.addAll(queryConditionInfo)
    return this
}

fun bracket(queryConditionInfo: Queue<QueryConditionInfo>): Queue<QueryConditionInfo> {
    val queue = LinkedList<QueryConditionInfo>()
    queue.add(QueryConditionInfo("(", QueryConditionType.Operator))
    queue.addAll(queryConditionInfo)
    queue.add(QueryConditionInfo(")", QueryConditionType.Operator))
    return queue
}

infix fun Queue<QueryConditionInfo>.or(queryConditionInfo: Queue<QueryConditionInfo>): Queue<QueryConditionInfo> {
    this.add(QueryConditionInfo("or", QueryConditionType.Operator))
    this.addAll(queryConditionInfo)
    return this
}


infix fun KProperty<String>.contains(value: String): Queue<QueryConditionInfo> {
    return internalLike(this.name, value, LikeType.Contains, this.getPackName())
}

infix fun KProperty<String>.startWith(value: String): Queue<QueryConditionInfo> {
    return internalLike(this.name, value, LikeType.StartWith, this.getPackName())
}


fun internalLike(
    name: String,
    value: String,
    likeType: LikeType,
    packageName: String
): Queue<QueryConditionInfo> {
    return LinkedList<QueryConditionInfo>().apply {
        add(QueryConditionInfo(name, QueryConditionType.Column, packageName))
        add(QueryConditionInfo("like", QueryConditionType.Operator))
        when (likeType) {
            LikeType.StartWith -> {
                add(
                    QueryConditionInfo(
                        StaticApp.commonUtils.startWithHandler(value),
                        QueryConditionType.Value
                    )
                )
            }

            LikeType.EndWith -> {
                add(
                    QueryConditionInfo(
                        StaticApp.commonUtils.endWithHandler(value),
                        QueryConditionType.Value
                    )
                )
            }

            LikeType.Contains -> {
                add(
                    QueryConditionInfo(
                        StaticApp.commonUtils.containHandler(value),
                        QueryConditionType.Value
                    )
                )
            }
        }
    }
}
infix fun KProperty<String>.endWith(value: String): Queue<QueryConditionInfo> {
    return internalLike(this.name, value, LikeType.EndWith, this.getPackName())
}

fun KProperty<*>.getPackName(): String {
    return this.javaField?.declaringClass?.name ?: ""
}


