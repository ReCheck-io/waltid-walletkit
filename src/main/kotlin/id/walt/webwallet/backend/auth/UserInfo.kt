package id.walt.webwallet.backend.auth

import kotlinx.serialization.Serializable

@Serializable
class UserInfo(
    val id: String
) {

    var email: String? = null
    var password: String? = null
    var token: String? = null
    var ethAccount: String? = null
    var did: String? = null

    init {
        when {
            id.contains("@") -> email = id
            id.lowercase().contains("0x") -> ethAccount = id
            id.lowercase().startsWith("did:") -> did = id
        }
    }

    constructor(id:String, password:String, did:String) : this(id) {
        this.password = password
        this.did = did
    }

}
