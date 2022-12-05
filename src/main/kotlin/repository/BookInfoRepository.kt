package repository

import entity.BookInfo
import java.sql.Statement

class BookInfoRepository(dataSource: DataSource) {

    val con = dataSource.connection

    fun addBookInfo(bookInfo: BookInfo): BookInfo {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val pstmt = con.prepareStatement(
            "INSERT INTO book_info(title, author, publisher, publish_year) values (?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        pstmt.setString(1, bookInfo.title)
        pstmt.setString(2, bookInfo.author)
        pstmt.setString(3, bookInfo.publisher)
        pstmt.setInt(4, bookInfo.publishYear)
        return BookInfo(
            bookNumber = pstmt.executeUpdate(),
            title = bookInfo.title,
            author = bookInfo.author,
            publisher = bookInfo.publisher,
            publishYear = bookInfo.publishYear
        )
    }
}