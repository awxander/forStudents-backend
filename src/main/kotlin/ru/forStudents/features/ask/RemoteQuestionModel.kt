package ru.forStudents.features.ask

import kotlinx.serialization.Serializable


@Serializable
data class RemoteQuestionModel(
    val topic: String,
    val body : String
)