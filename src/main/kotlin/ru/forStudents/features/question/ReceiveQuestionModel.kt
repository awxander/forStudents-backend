package ru.forStudents.features.question

import kotlinx.serialization.Serializable


@Serializable
data class ReceiveQuestionModel(
    val topic: String,
    val body : String
)