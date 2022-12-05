package entity

data class BookInfo(
    val bookNumber: Int? = null,
    val title: String,
    val author: String,
    val publisher: String,
    val publishYear: Int
)