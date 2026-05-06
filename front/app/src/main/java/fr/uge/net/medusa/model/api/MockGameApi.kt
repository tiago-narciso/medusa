package fr.uge.net.medusa.model.api

class MockGameApi: IGameApi {
    override suspend fun login(
        username: String,
        password: String
    ): LoginResult {
        println("Logging in with username $username and password $password")
        if (username == "network" && password == "network") {
            return LoginResult.Error.NetworkError("Network error")
        }

        if (username != "admin" || password != "admin") {
            return LoginResult.Error.InvalidCredentials
        }
        return LoginResult.Success("token")
    }
}