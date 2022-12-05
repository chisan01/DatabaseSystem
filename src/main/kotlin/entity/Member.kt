package entity

import java.sql.Date

data class Member(
    val memberId: Int,
    val name: String,
    val password: String,
    val returnDate: Date?,
    val job: String
)