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

    var connection: Connection? = null
        get() {
            connection?.apply { return this }

            Class.forName("com.mysql.cj.jdbc.Driver")
            field = DriverManager.getConnection(
                "jdbc:mysql://$ServerIPAddress:$ServerPortNum/$DATABASE_NAME",
                dbUser,
                dbPassword
            )
            return field
        }

    fun releaseConnection() {
        connection?.close()
    }
}