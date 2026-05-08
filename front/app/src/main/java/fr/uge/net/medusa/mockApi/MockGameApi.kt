package fr.uge.net.medusa.mockApi

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
        username: String,
        password: String
    ): LoginResult {
        if (username == "network" && password == "network") {
            return LoginResult.Error.NetworkError("Network error")
        }

        if (username != "admin" || password != "admin") {
            return LoginResult.Error.InvalidCredentials
        }
        return LoginResult.Success("token")
    }
}