package ru.forStudents.features.question

import kotlinx.serialization.Serializable

@Serializable
data class ResponseQuestionModel(
    val email : String,
    val userName: String,
    val topic : String,
    val body: String
)