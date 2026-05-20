package fr.uge.net.medusa.api

import fr.uge.net.medusa.models.CardsResponse
import fr.uge.net.medusa.models.LoginRequest
import fr.uge.net.medusa.models.LoginResponse
import fr.uge.net.medusa.models.NearRequest
import fr.uge.net.medusa.models.NearResponse
import fr.uge.net.medusa.models.RegisterRequest
import fr.uge.net.medusa.models.RegisterResponse

/**
 * Defines the game API contract.
 *
 * Any class implementing this interface must provide
 * the login function.
 *
 * Examples:
 * - MockGameApi
 * - RetrofitGameApi
 *
 * The login function is marked as suspend because
 * login operations are usually asynchronous
 * (network/database operations).
 */

interface IGameApi {
    suspend fun login(request: LoginRequest): LoginResponse // returns the login token
    suspend fun register(request: RegisterRequest): RegisterResponse // returns the login token
    suspend fun getCards(): CardsResponse

    suspend fun near(request: NearRequest): NearResponse


}

