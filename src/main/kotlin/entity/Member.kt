package entity

import java.sql.Date

data class Member(
    val memberId: Int? = null,
    val name: String,
    val password: String,
    val suspendFinishDate: Date? = null,
    val job: Job
)

enum class Job {
    UNIVERSITY_STUDENT, GRADUATE_STUDENT, PROFESSOR;

    val maxBorrowCnt: Int
        get() = listOf(1, 2, 5)[ordinal]
}