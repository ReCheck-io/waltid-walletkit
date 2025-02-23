package id.walt

import com.beust.klaxon.JsonObject
import id.walt.servicematrix.ServiceMatrix
import id.walt.servicematrix.ServiceRegistry
import id.walt.services.context.ContextManager
import id.walt.webwallet.backend.auth.JWTService
import id.walt.webwallet.backend.auth.UserInfo
import id.walt.webwallet.backend.context.WalletContextManager
import io.javalin.Javalin
import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import mu.KotlinLogging


private val log = KotlinLogging.logger {}

abstract class BaseApiTest : AnnotationSpec() {

    val waltContext = WalletContextManager
    val host = "localhost"
    val port = 7777
    val url = "http://$host:$port"
    var server: Javalin? = null
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        expectSuccess = false
    }
    val email = "test@walt.id"
    val did = "did:web:issuer.ssikit.org"

    @BeforeAll
    fun init() {
        ServiceMatrix("service-matrix.properties")
        ServiceRegistry.registerService<ContextManager>(waltContext)
    }

    @BeforeClass
    fun startServer() {
        server = Javalin.create { config ->
            config.apply {
                enableDevLogging()
                requestLogger { ctx, ms ->
                    log.debug { "Received: ${ctx.body()} - Time: ${ms}ms" }
                }
                accessManager(JWTService)
            }
        }.apply {
            before(JWTService.jwtHandler)
            before(waltContext.preRequestHandler)
            after(waltContext.postRequestHandler)
            routes {
                loadRoutes()
            }
        }.start("127.0.0.1", port)

    }

    abstract fun loadRoutes()

    @AfterClass
    fun teardown() {
        server?.stop()
    }

    //TODO: Sign up check before the login check
    fun authenticate(): UserInfo = runBlocking {
        val userInfo = client.post("$url/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                "id" to email,
                "email" to email,
                "password" to "1234"
            ))
        }.body<UserInfo>()
        return@runBlocking userInfo
    }

    fun authenticateDid(): UserInfo = runBlocking {
        val userInfo = client.post("$url/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "id" to did,
                "did" to did,
                "password" to "1234"
            ))
        }.body<UserInfo>()
        return@runBlocking userInfo
    }
}
