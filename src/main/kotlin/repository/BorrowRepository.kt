package repository

import entity.Borrow
import java.sql.Date
import java.sql.ResultSet
import java.sql.Statement

class BorrowRepository(dataSource: DataSource) {

    val con = dataSource.connection

    fun save(borrow: Borrow): Borrow {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val pstmt = con.prepareStatement(
            "INSERT INTO borrow(member_id, serial_number, borrow_start_date) values (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        pstmt.setInt(1, borrow.memberId)
        pstmt.setInt(2, borrow.serialNumber)
        pstmt.setDate(3, borrow.borrowStartDate)
        return Borrow(
            id = pstmt.executeUpdate(),
            memberId = borrow.memberId,
            serialNumber = borrow.serialNumber,
            borrowStartDate = borrow.borrowStartDate
        )
    }

    fun findAllByMemberId(memberId: Int): List<Borrow> {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val stmt = con.prepareStatement("SELECT * FROM borrow WHERE member_id = ?")
        stmt.setInt(1, memberId)
        val rs = stmt.executeQuery()

        val result = mutableListOf<Borrow>()
        while (rs.next()) {
            result.add(createBorrowFromResultSet(rs))
        }
        return result
    }

    fun findAllBySerialNumber(serialNumber: Int): List<Borrow> {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val stmt = con.prepareStatement("SELECT * FROM borrow WHERE serial_number = ?")
        stmt.setInt(1, serialNumber)
        val rs = stmt.executeQuery()

        val result = mutableListOf<Borrow>()
        while (rs.next()) {
            result.add(createBorrowFromResultSet(rs))
        }
        return result
    }

    fun findByMemberIdAndSerialNumber(memberId: Int, serialNumber: Int): List<Borrow> {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val stmt = con.prepareStatement("SELECT * FROM borrow WHERE member_id = ? AND serial_number = ?")
        stmt.setInt(1, memberId)
        stmt.setInt(2, serialNumber)
        val rs = stmt.executeQuery()

        val result = mutableListOf<Borrow>()
        while (rs.next()) {
            result.add(createBorrowFromResultSet(rs))
        }
        return result
    }

    fun returnBook(memberId: Int, serialNumber: Int, returnDate: Date) {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val stmt = con.prepareStatement(
            "UPDATE borrow SET return_date = ? " +
                    "WHERE member_id = ? " +
                    "AND serial_number = ? " +
                    "AND return_date IS NULL"
        )
        stmt.setDate(1, returnDate)
        stmt.setInt(2, memberId)
        stmt.setInt(3, serialNumber)
        stmt.execute()
    }

    private fun createBorrowFromResultSet(rs: ResultSet): Borrow {
        return Borrow(
            id = rs.getInt(1),
            memberId = rs.getInt(2),
            serialNumber = rs.getInt(3),
            borrowStartDate = rs.getDate(4),
            countOfDueDateExtension = rs.getInt(5),
            returnDate = rs.getDate(6)
        )
    }
}