package ru.forStudents.features.ask

import kotlinx.serialization.Serializable


@Serializable
data class RemoteQuestionModel(
    val question : String,
    val questionTopic: String
)