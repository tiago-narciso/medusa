package fr.uge.net.medusa.model.api

interface IGameApi {
    suspend fun login(username: String, password: String): LoginResult // returns the login token
    suspend fun register(username: String, password: String): RegisterResult // returns the login token
}

interface LoginResult {
    // The success case containing the return value
    data class Success(val authToken: String) : LoginResult

    sealed interface Error : LoginResult {
        data object InvalidCredentials : Error
        data class NetworkError(val message: String) : Error
        data class UnknownError(val message: String) : Error
    }
}

interface RegisterResult {
    // The success case containing the return value
    data class Success(val authToken: String) : RegisterResult

    sealed interface Error : RegisterResult {
        data object InvalidPassword : Error
        data object LoginAlreadyTaken : Error
        data class NetworkError(val message: String) : Error
        data class UnknownError(val message: String) : Error
    }
}