package fr.uge.net.medusa.api

import fr.uge.net.medusa.models.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    /**
     * Sends a POST login request to the backend API.
     * @param LoginRequest Contains user login credentials.
     * @return LoginResponse containing either:
     * - authentication token on success
     * - error information on failure
     */
    @POST("user/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    /**
     * Sends a POST register request to the backend API.
     * @param RegisterRequest Contains user register credentials.
     * @return RegisterResponse containing either:
     * - authentication token on success
     * - error information on failure
     */
    @POST("user/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse
}
