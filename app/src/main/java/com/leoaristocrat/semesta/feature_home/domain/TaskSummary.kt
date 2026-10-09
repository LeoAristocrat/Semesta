package com.leoaristocrat.semesta.feature_home.domain

data class TaskSummary(
    val id: String,
    val title: String,
    val dueText: String,
    val estimatedTimeText: String
)
