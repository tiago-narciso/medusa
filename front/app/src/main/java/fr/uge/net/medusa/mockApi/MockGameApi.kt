package fr.uge.net.medusa.mockApi

import fr.uge.net.medusa.api.IGameApi

import fr.uge.net.medusa.models.*

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
}