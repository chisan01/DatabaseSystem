package entity

import java.sql.Timestamp
import java.util.concurrent.SubmissionPublisher

data class BookInfo(
    val bookNumber: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val publishYear: String
)