package entity

import java.sql.Timestamp

data class BookReservation(
    val memberId: Int,
    val bookNumber: Int,
    val reservationDate: Timestamp
)