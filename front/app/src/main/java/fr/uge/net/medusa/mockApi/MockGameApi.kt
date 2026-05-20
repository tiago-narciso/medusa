package fr.uge.net.medusa.mockApi

import fr.uge.net.medusa.api.IGameApi
import fr.uge.net.medusa.data.Card

import fr.uge.net.medusa.models.*
import java.util.UUID

/**
 * mock implementation of the game API.
 *
 * This class simulates backend behavior locally
 * without using a real server or internet connection.
 *
 * Used for:
 * - testing UI logic
 * - testing login flow
 * - simulating errors
 * - development without backend
 *
 */
class MockGameApi: IGameApi {
    override suspend fun login(
        request: LoginRequest
    ): LoginResponse {
        if (request.login == "network" && request.password == "network") {
            throw Exception("network error")
        }

        if (request.login != "admin" || request.password!= "admin") {
            throw Exception("invalid credentials")
        }
        return LoginResponse(token = "token")
    }

    override suspend fun register(
        request: RegisterRequest

    ): RegisterResponse {
        if (request.login == "taken" && request.password == "taken") {
            throw Exception(" Login already taken")
        }
        if (request.password.length < 2) {
            throw Exception(" Invalid response")
        }
        return RegisterResponse(token = "token")
    }

    override suspend fun getCards():
            CardsResponse {

        return CardsResponse(
            cards = listOf(
                Card(
                    wikidataId = UUID.randomUUID(),
                    uniqueId = UUID.randomUUID(),
                    placeOfBirth = "Paris",
                    power = 120,
                    acquisitionDate = "2025-07-21T14:30:00Z",
                    personality =  "naruto"
                ),
                Card(
                    wikidataId = UUID.randomUUID(),
                    uniqueId = UUID.randomUUID(),
                    placeOfBirth = "Paris",
                    power = 80,
                    acquisitionDate = "2024-07-21T14:30:00Z",
                    personality =  "naruto"

                ),

                Card(
                    wikidataId = UUID.randomUUID(),
                    uniqueId = UUID.randomUUID(),
                    placeOfBirth = "Tokyo",
                    power = 150,
                    acquisitionDate = "2023-07-21T14:30:00Z",
                    personality =  "naruto"

                ),

                Card(
                    wikidataId = UUID.randomUUID(),
                    uniqueId = UUID.randomUUID(),
                    placeOfBirth = "Rome",
                    power = 60,
                    acquisitionDate = "2022-07-21T14:30:00Z",
                    personality =  "naruto"

                )
            )
        )
    }
}