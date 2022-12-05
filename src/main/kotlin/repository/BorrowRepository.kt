package repository

import entity.Borrow
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
            result.add(
                Borrow(
                    id = rs.getInt(1),
                    memberId = rs.getInt(2),
                    serialNumber = rs.getInt(3),
                    borrowStartDate = rs.getDate(4),
                    countOfDueDateExtension = rs.getInt(5),
                    returnDate = rs.getDate(6)
                )
            )
        }
        return result
    }
}