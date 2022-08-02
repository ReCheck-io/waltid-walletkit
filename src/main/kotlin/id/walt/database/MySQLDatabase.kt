package id.walt.database

import id.walt.webwallet.backend.auth.UserInfo
import org.apache.commons.codec.digest.DigestUtils
import java.sql.Connection
import java.sql.DriverManager

object GFG {
    @JvmStatic
    fun main(arg: Array<String>) {

    }
}


fun encrypt(originalString: String): String = DigestUtils.sha256Hex(originalString);

fun insertRow(username: String, password: String, did: String) {
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/uoi_backend?useSSL=FALSE&serverTimezone=UTC",
            "daka", "daka"
        )
        val sql = "INSERT INTO wallet.users VALUES (\"$username\", \"$password\", \"$did\")"
        println(sql)
        with(connection) {
            createStatement().execute(sql)
        }
    } catch (exception: Exception) {
        println(exception)
    }
}

fun queryUsers() {
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/uoi_backend?useSSL=FALSE&serverTimezone=UTC",
            "daka", "daka"
        )
        val sql = "SELECT * FROM wallet.users"
        val rs = connection.createStatement().executeQuery(sql)
        while (rs.next()) {
            println(
                "username: ${rs.getString("username")}\t" +
                        "password: $${rs.getString("password")}\t" +
                        "did: ${rs.getString("did")}"
            )
        }
    } catch (exception: Exception) {
        println(exception)
    }
}

fun queryUser(username: String): UserInfo {
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/uoi_backend?useSSL=FALSE&serverTimezone=UTC",
            "daka", "daka"
        )
        val sql = "SELECT * FROM wallet.users WHERE username=\"$username\""
        val rs = connection.createStatement().executeQuery(sql)
        // Takes the first element that meets the requirement, which should be the only, as username should be unique
        rs.first()

        println(
            "username: ${rs.getString("username")}\t" +
                    "password: ${rs.getString("password")}\t" +
                    "did: ${rs.getString("did")}"
        )
        val user =
            UserInfo(id = rs.getString("username"), password = rs.getString("password"), did = rs.getString("did"))
        return user
    } catch (exception: Exception) {
        println(exception)
        throw exception
    }
}