package fr.uge.net.medusa.models

/**
 * JSON body sent to users/login endpoint.
 */

data class LoginRequest(
    val login: String,
    val password: String
)