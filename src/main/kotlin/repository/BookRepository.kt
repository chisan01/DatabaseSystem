package repository

import entity.Book
import entity.BookPrint
import entity.Borrow
import java.sql.ResultSet
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
        pstmt.executeUpdate()
        return Book(
            serialNumber = pstmt.generatedKeys.run { if(next()) getInt(1) else null },
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

    fun printAll(): List<BookPrint> {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val stmt = con.prepareStatement("select a.serial_number, b.title, b.author, b.publisher, b.publish_year " +
                "from book a join book_info b on a.book_number = b.book_number;")
        val rs = stmt.executeQuery()

        val result = mutableListOf<BookPrint>()
        while (rs.next()) {
            result.add(createPrintBookFromResultSet(rs))
        }
        return result
    }


    private fun createPrintBookFromResultSet(rs: ResultSet): BookPrint {
        return BookPrint(
            serialNumber = rs.getInt(1),
            title = rs.getString(2),
            author = rs.getString(3),
            publisher = rs.getString(4),
            publishYear = rs.getInt(5),
        )
    }
}