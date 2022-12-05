package repository

import entity.Job
import entity.Member
import java.sql.Statement

class MemberRepository(dataSource: DataSource) {

    val con = dataSource.connection

    fun save(member: Member): Member {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val pstmt = con.prepareStatement(
            "INSERT INTO member(name, password, job) values (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        pstmt.setString(1, member.name)
        pstmt.setString(2, member.password)
        pstmt.setString(3, member.job.toString())
        return Member(
            memberId = pstmt.executeUpdate(),
            name = member.name,
            password = member.password,
            job = member.job
        )
    }

    fun findByMemberId(memberId: Int): Member? {
        if (con == null) throw Exception("데이터베이스 연결 실패")

        val stmt = con.prepareStatement("SELECT * FROM member WHERE member_id = ?")
        stmt.setInt(1, memberId)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            return Member(
                memberId = rs.getInt(1),
                name = rs.getString(2),
                password = rs.getString(3),
                suspendFinishDate = rs.getDate(4),
                job = Job.valueOf(rs.getString(5))
            )
        }
        return null
    }
}