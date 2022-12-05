package entity

enum class Job {
    UNIVERSITY_STUDENT, GRADUATE_STUDENT, PROFESSOR;

    val maxBorrowCnt: Int
        get() = listOf(1, 2, 5)[ordinal]
}