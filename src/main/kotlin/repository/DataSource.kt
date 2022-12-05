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
            if(_connection == null) throw Exception("데이터베이스 연결 실패")
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
}