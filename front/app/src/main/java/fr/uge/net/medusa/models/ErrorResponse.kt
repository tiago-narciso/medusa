package fr.uge.net.medusa.models
import com.google.gson.Gson

/**
 * Represents an error response returned by the backend API.
 *
 * Example backend JSON:
 * {
 *     "error": "Invalid credentials"
 * }
 * This class is used to:
 * - store backend error messages
 * - convert JSON error responses into Kotlin objects
 * using Gson
 */
data class ErrorResponse(
    val error: String
){
    companion object{

        /**
         *
         * Parses the error returned from the back into an ErrorResponse object
         * @param json Raw JSON string returned by the backend.
         * @return Parsed ErrorResponse object, or null if parsing fails.
         */
        fun parseError(json: String?): ErrorResponse?{
            return try{
                Gson().fromJson(
                    json,
                    ErrorResponse::class.java
                )
            }catch(e: Exception){
               null
            }
        }

    }
}
