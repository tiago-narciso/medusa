package fr.uge.net.medusa.model.api

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