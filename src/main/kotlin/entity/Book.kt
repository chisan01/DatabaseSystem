package entity

data class Book(
    val serialNumber: Int? = null,
    val bookNumber: Int
)

enum class BookStatus {
    REMAIN, BORROWED, DAMAGED
}