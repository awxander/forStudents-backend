package ru.forStudents.database.questions

data class QuestionDTO(
    val id : String,
    val question: String,
    val topic: String,
    val userEmail: String,
    val discussionId : String
)