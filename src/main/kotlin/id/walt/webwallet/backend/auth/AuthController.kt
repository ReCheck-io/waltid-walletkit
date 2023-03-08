package id.walt.webwallet.backend.auth

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.fasterxml.jackson.databind.ObjectMapper
import id.walt.crypto.KeyAlgorithm
import id.walt.database.encrypt
import id.walt.database.insertRow
import id.walt.database.queryUser
import id.walt.database.updateDid
import id.walt.model.DidMethod
import id.walt.services.context.ContextManager
import id.walt.services.did.DidService
import id.walt.services.key.KeyFormat
import id.walt.services.key.KeyService
import id.walt.services.keystore.KeyType
import id.walt.webwallet.backend.config.WalletConfig
import id.walt.webwallet.backend.context.WalletContextManager
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import io.javalin.plugin.openapi.dsl.documented
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

object AuthController {
    val routes
        get() = path("auth") {
            path("login") {
                post(documented(document().operation {
                    it.summary("Login")
                        .operationId("login")
                        .addTagsItem("Authentication")
                }
                    .body<UserInfo> { it.description("Login info") }
                    .json<UserInfo>("200"),
                    AuthController::login), UserRole.UNAUTHORIZED)
            }
            path("signup") {
                post(documented(document().operation {
                    it.summary("Signup")
                        .operationId("signup")
                        .addTagsItem("Authentication")
                }
                    .body<UserInfo> { it.description("Signup info") }
                    .json<UserInfo>("200"),
                    AuthController::signup), UserRole.UNAUTHORIZED)
            }
            path("userInfo") {
                get(
                    documented(document().operation {
                        it.summary("Get current user info")
                            .operationId("userInfo")
                            .addTagsItem("Authentication")
                    }
                        .json<UserInfo>("200"),
                        AuthController::userInfo), UserRole.AUTHORIZED)
            }
        }


    fun signup(ctx: Context) {
        val userInfo = ctx.bodyAsClass(UserInfo::class.java)
        val id = if (userInfo.id.length > 0) userInfo.id
        else userInfo.email
        val encryptedPass = encrypt(userInfo.password!!)

        insertRow(id!!, encryptedPass, userInfo.did ?: "null", 0);
    }

    fun login(ctx: Context) {
        val userInfo = ctx.bodyAsClass(UserInfo::class.java)

        // make the db check with the email
        val id = if (userInfo.id.length > 0) userInfo.id
        else userInfo.email

        // check if the user is signed with their email in the db
        val user = queryUser(id!!) ?: throw IllegalArgumentException("The user has not been registered")
        val checkPass = encrypt(userInfo.password!!) == user.password

        if (checkPass) {
            ContextManager.runWith(WalletContextManager.getUserContext(userInfo)) {
                // TODO: Create DID and Register it to the EBSI DID Registry
                if (!userInfo.did.isNullOrEmpty()) {
                    val key = KeyService.getService().generate(KeyAlgorithm.ECDSA_Secp256k1)
                    val did = DidService.create(DidMethod.ebsi, key.id)
                    println("did $did")

                    val privKeyStr = KeyService.getService().export(key.id, KeyFormat.JWK, KeyType.PRIVATE)
//                    println("privKeyStr $privKeyStr")

                    val json = parseJsonString(privKeyStr)
                    val encodedPrivKey: String = Base64
                        .getEncoder()
                        .encodeToString(json.string("d")!!.toByteArray())

                    println("encodedPrivKey $encodedPrivKey")

                    if (did.isNullOrEmpty() || encodedPrivKey.isNullOrEmpty()) {
                        // TODO: Throw proper Error
                        println("$did | $encodedPrivKey")
                        throw IllegalArgumentException("Error")
                    } else {
                        registerDID(did, encodedPrivKey)

                        // TODO: Update the DID in the database in the correct manner
                        updateDid(user.id, did)
                    }
                }
            }
            ctx.json(UserInfo(userInfo.id).apply {
                token = JWTService.toJWT(userInfo)
            })
        } else {
            throw IllegalArgumentException("The user has not been registered")
        }

    }

    fun userInfo(ctx: Context) {
        ctx.json(JWTService.getUserInfo(ctx)!!)
    }

    private fun registerDID(did: String, privateKey: String): Any {
        val body = mapOf("did" to did, "privateKey" to privateKey)
        val url = WalletConfig.config.bridgeApiUrl + "/did/register"

        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(body)

        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .header("Content-Type", "application/json")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // TODO: Based on response throw Error or return success to the UI
        val responseBody = parseJsonString(response.body())
        println(responseBody)

        return responseBody["status"] == true
    }

    private fun parseJsonString(str: String): JsonObject {
        val parser: Parser = Parser.default()
        val stringBuilder: StringBuilder = StringBuilder(str)

        return parser.parse(stringBuilder) as JsonObject
    }
}
