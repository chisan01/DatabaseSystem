package entity

import java.sql.Date

data class Borrow(
    val id: Int,
    val memberId: Int,
    val serialNumber: Int,
    val borrowStartDate: Date,
    val countOfDueDateExtension: Int = 0,
    val returnDate: Date?
)