package fr.uge.net.medusa.api

import fr.uge.net.medusa.models.*

class RealGameApi : IGameApi {

    private val apiService =
        ApiClient.getApiService()

    override suspend fun login(
        request: LoginRequest,
    ): LoginResponse {

        return apiService.login(
            request
        )
    }

    override suspend fun register(
        request: RegisterRequest
    ): RegisterResponse {

        return apiService.register(
            request
        )
    }

    override suspend fun getCards(): CardsResponse {
        return apiService.getCards()
    }
}