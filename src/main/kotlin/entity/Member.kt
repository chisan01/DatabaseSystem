package entity

import java.sql.Date

data class Member(
    val memberId: Int? = null,
    val name: String,
    val password: String,
    val suspendFinishDate: Date? = null,
    val job: Job
)