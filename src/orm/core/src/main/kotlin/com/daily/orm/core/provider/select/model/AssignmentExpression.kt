package com.daily.orm.core.provider.select.model

import com.daily.orm.core.provider.select.enums.JoinType

class AssignmentExpression {

    lateinit var joinType: JoinType

    lateinit var leftTable : String

    lateinit var leftTableAlias : String

    lateinit var leftColumn : String

    lateinit var rightTable : String

    lateinit var rightColumn : String

    lateinit var rightTableAlias : String

}