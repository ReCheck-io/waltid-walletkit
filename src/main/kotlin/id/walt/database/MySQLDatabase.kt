package id.walt.database

import java.sql.Connection
import java.sql.DriverManager

object GFG {
    @JvmStatic
    fun main(arg: Array<String>) {

        var connection: Connection? = null
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/uoi_backend?useSSL=FALSE&serverTimezone=UTC",
                "daka", "daka"
            )

//            insertRow(connection, "wallet", "users","DAKATA", "passDaka", "didDaka")

            queryRows(connection,"wallet","users")
            println("--------")
            queryUser(connection, "wallet", "users", "DAKATA")
        } catch (exception: Exception) {
            println(exception)
        }
    }
}

fun insertRow(connection: Connection, schema : String, table : String, username: String, password: String, did: String) {
    val sql = "INSERT INTO $schema.$table VALUES (\"$username\", \"$password\", \"$did\")"
    println(sql)
    with(connection) {
        createStatement().execute(sql)
    }
}

fun queryRows(connection: Connection, schema : String, table : String) {
    val sql = "SELECT * FROM $schema.$table"
    val rs = connection.createStatement().executeQuery(sql)
    while (rs.next()) {
        println("username: ${rs.getString("username")}\t" +
                "password: $${rs.getString("password")}\t" +
                "did: ${rs.getString("did")}")
    }
}

fun queryUser(connection: Connection, schema : String, table : String, username:String) {
    val sql = "SELECT * FROM $schema.$table WHERE username=\"$username\""
    val rs = connection.createStatement().executeQuery(sql)
    while (rs.next()) {
        println("username: ${rs.getString("username")}\t" +
                "password: ${rs.getString("password")}\t" +
                "did: ${rs.getString("did")}")
    }
}