package com.sls.handbook.core.model

data class PatternContent(
    val title: String,
    val subtitle: String,
    val description: String,
    val whenToUse: List<String>,
    val androidExamples: String,
    val structure: String,
)
