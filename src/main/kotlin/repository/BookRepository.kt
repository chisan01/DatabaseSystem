package repository

import entity.Book
import java.sql.Statement

class BookRepository(dataSource: DataSource) {

    val con = dataSource.connection

    fun save(book: Book): Book {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val pstmt = con.prepareStatement(
            "INSERT INTO book(book_number) values (?)",
            Statement.RETURN_GENERATED_KEYS
        )
        pstmt.setInt(1, book.bookNumber)
        return Book(
            serialNumber = pstmt.executeUpdate(),
            bookNumber = book.bookNumber
        )
    }

    fun findBySerialNumber(serialNumber: Int): Book? {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val stmt = con.prepareStatement("SELECT * FROM book WHERE serial_number = ?")
        stmt.setInt(1, serialNumber)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            return Book(
                serialNumber = rs.getInt(1),
                bookNumber = rs.getInt(2)
            )
        }
        return null
    }
}