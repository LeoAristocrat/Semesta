package com.leoaristocrat.semesta.feature_home.domain

import com.leoaristocrat.semesta.feature_grades.domain.SubjectVisualType

data class SubjectSummary(
    val id: String,
    val name: String,
    val average: Double?,
    val targetAverage: Double,
    val progress: Float,
    val type: SubjectVisualType
)
