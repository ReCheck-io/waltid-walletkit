package id.walt.database

import id.walt.issuer.backend.IssuableCredential
import id.walt.webwallet.backend.auth.UserInfo
import org.apache.commons.codec.digest.DigestUtils
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

val connectionURL = "jdbc:mysql://localhost:3306/wallet?useSSL=FALSE&allowPublicKeyRetrieval=true&serverTimezone=UTC"
val user = "daka"
val pass = "Admin@123"
object GFG {
    @JvmStatic
    fun main(arg: Array<String>) {

    }
}


fun encrypt(originalString: String): String = DigestUtils.sha256Hex(originalString);

fun insertRow(username: String, password: String, did: String, issuerID: Int) {
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        println(connectionURL)
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            connectionURL, user, pass
        )
        val sql = "INSERT INTO wallet.users VALUES (\"$username\", \"$password\", \"$did\", \"$issuerID\" )"
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
            connectionURL, user, pass
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

fun updateDid(username: String, did:String){
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            connectionURL, user, pass
        )
        val sql = "UPDATE wallet.users" +
                " SET did = \"$did\"" +
                " WHERE username = \"$username\";"
        println(sql)
        with(connection) {
            createStatement().execute(sql)
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
            connectionURL, user, pass
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

fun insertSession(sessionID: String, issuerID: String, did: String, issuanceID: String,schemaIDs: String, walletRedirectUri: String ) {
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
           connectionURL, user, pass
        )
        val sql = "INSERT INTO wallet.sessions VALUES (\"$sessionID\", \"$did\", \"$issuerID\", \"$issuanceID\", \"$schemaIDs\", \"$walletRedirectUri\")"
        println(sql)
        with(connection) {
            createStatement().execute(sql)
        }
    } catch (exception: Exception) {
        println(exception)
    }
}

fun insertIssuance(issuanceID: String, id: String, sessionID: String, vc:String) {
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
           connectionURL, user, pass
        )
        println("do tuk ?")
        val sql = "INSERT INTO wallet.issuances VALUES (\"$issuanceID\", \"$id\", \"$sessionID\", \"$vc\")"
        println(sql)
        with(connection) {
            createStatement().execute(sql)
        }
    } catch (exception: Exception) {
        println(exception)
    }
}

fun updateIssuanceSessionIDWithSessionID(issuanceID: String, sessionID: String){
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            connectionURL, user, pass
        )
        val sql = "UPDATE wallet.issuances" +
                " SET sessionID = \"$sessionID\"" +
                " WHERE issuanceID = \"$issuanceID\";"
        println(sql)
        with(connection) {
            createStatement().execute(sql)
        }
    } catch (exception: Exception) {
        println(exception)
    }
}

fun getAllIssuancesReadyForApprovement(): List<IssuanceData>? {
    var listOfIssuances = mutableListOf<IssuanceData>()
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            connectionURL, user, pass
        )
        val sql = "SELECT * FROM wallet.issuances"
        val rs = connection.createStatement().executeQuery(sql)
        while (rs.next()) {
            println(
                "Issuance: ${rs.getString("issuanceID")}\t" +
                        "Id: $${rs.getString("id")}\t" +
                        "sessionID: ${rs.getString("sessionID")}" +
                        "sessionID: ${rs.getString("vc")}"
            )
            val issuanceRequest = IssuanceData(rs.getString("issuanceID"), rs.getString("id"), rs.getString("sessionID"), rs.getString("vc"))
            println(issuanceRequest)
            listOfIssuances?.add(issuanceRequest)
            println(listOfIssuances)
        }

        return listOfIssuances
    } catch (exception: Exception) {
        println(exception)
    }

    return listOfIssuances
}

fun deleteIssuanceRequest(issuanceID:String){
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            connectionURL, user, pass
        )
        val sql = "DELETE FROM wallet.issuances" +
                " WHERE issuanceID = \"$issuanceID\";"
        println(sql)
        with(connection) {
            createStatement().execute(sql)
        }
    } catch (exception: Exception) {
        println(exception)
    }
}

fun insertVC(sessionID: String, vc: String) {
    var connection: Connection? = null
    try {
        // below two lines are used for connectivity.
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            connectionURL, user, pass
        )
        val sql = "UPDATE wallet.issuances" +
                " SET vc = \'$vc\'" +
                " WHERE sessionID = \"$sessionID\";"
        println(sql)
        with(connection) {
            createStatement().execute(sql)
        }
    } catch (exception: Exception) {
        println(exception)
    }
}
