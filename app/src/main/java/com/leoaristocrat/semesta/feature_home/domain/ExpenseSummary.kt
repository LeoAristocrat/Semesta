package com.leoaristocrat.semesta.feature_home.domain

data class ExpenseSummary(
    val transport: Int,
    val food: Int,
    val total: Int,
    val chartValues: List<Int>
)
