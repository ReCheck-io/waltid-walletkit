package id.walt.webwallet.backend.auth

import id.walt.database.encrypt
import id.walt.database.insertRow
import id.walt.database.queryUser
import id.walt.model.DidMethod
import id.walt.services.context.ContextManager
import id.walt.services.did.DidService
import id.walt.webwallet.backend.context.WalletContextManager
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context
import io.javalin.plugin.openapi.dsl.document
import io.javalin.plugin.openapi.dsl.documented

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
        val id = if (userInfo.id.length>0) userInfo.id
        else userInfo.email
        val encryptedPass = encrypt(userInfo.password!!)

        insertRow(id!!, encryptedPass, userInfo.did ?: "null")
    }

    fun login(ctx: Context) {
        val userInfo = ctx.bodyAsClass(UserInfo::class.java)
        val id = if (userInfo.id.length>0) userInfo.id
        else userInfo.email
        val user = queryUser(id!!) ?: throw  IllegalArgumentException("The user has not been registered")
        val checkPass = encrypt(userInfo.password!!) == user.password
        if(checkPass){
            ContextManager.runWith(WalletContextManager.getUserContext(userInfo)) {
                if (DidService.listDids().isEmpty()) {
                    DidService.create(DidMethod.key)
                }
//            TODO: Update the DID in the database
            }
            ctx.json(UserInfo(userInfo.id).apply {
                token = JWTService.toJWT(userInfo)
            })
        }else {
            throw IllegalArgumentException("The user has not been registered")
        }

    }

    fun userInfo(ctx: Context) {
        ctx.json(JWTService.getUserInfo(ctx)!!)
    }
}
