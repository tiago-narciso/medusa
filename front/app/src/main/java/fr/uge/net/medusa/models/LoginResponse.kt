package fr.uge.net.medusa.models

/**
 * Response returned by backend after login request.
 */
data class LoginResponse(

    val success: Boolean,
    val token: String?,
    val error: String?
)