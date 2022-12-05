package repository

import java.sql.Connection
import java.sql.DriverManager

const val DATABASE_NAME = "library"

class DataSource(
    private val ServerIPAddress: String,
    private val ServerPortNum: String,
    private val dbUser: String,
    private val dbPassword: String
) {

    var _connection: Connection? = null

    val connection: Connection?
        get() {
            _connection?.apply { return this }

            Class.forName("com.mysql.cj.jdbc.Driver")
            _connection = DriverManager.getConnection(
                "jdbc:mysql://$ServerIPAddress:$ServerPortNum/$DATABASE_NAME",
                dbUser,
                dbPassword
            )
            if (_connection == null) throw Exception("데이터베이스 연결 실패")
            return _connection
        }

    @Override
    fun finalize() {
        releaseConnection()
    }

    private fun releaseConnection() {
        println("데이터베이스 연결 해제")
        connection?.close()
        _connection = null
    }

    fun deleteAll() {
        if (connection == null) throw Exception("데이터베이스 연결 실패")

        val stmt = connection!!.createStatement()
        stmt.execute("SET foreign_key_checks = 0")

        stmt.execute("DELETE FROM member")
        stmt.execute("ALTER TABLE member AUTO_INCREMENT = 1")
        stmt.execute("DELETE FROM admin")
        stmt.execute("ALTER TABLE admin AUTO_INCREMENT = 1")
        stmt.execute("DELETE FROM book_info")
        stmt.execute("ALTER TABLE book_info AUTO_INCREMENT = 1")
        stmt.execute("DELETE FROM book")
        stmt.execute("ALTER TABLE book AUTO_INCREMENT = 1")
        stmt.execute("DELETE FROM interested_in")
        stmt.execute("ALTER TABLE interested_in AUTO_INCREMENT = 1")
        stmt.execute("DELETE FROM borrow")
        stmt.execute("ALTER TABLE borrow AUTO_INCREMENT = 1")
        stmt.execute("DELETE FROM book_reservation")
        stmt.execute("ALTER TABLE book_reservation AUTO_INCREMENT = 1")
        stmt.execute("DELETE FROM damaged_book")
        stmt.execute("ALTER TABLE damaged_book AUTO_INCREMENT = 1")

        stmt.execute("SET foreign_key_checks = 1")
    }
}