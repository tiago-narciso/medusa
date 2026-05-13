package fr.uge.net.medusa.utils

import android.content.Context
import android.widget.Toast
import fr.uge.net.medusa.models.ErrorResponse
import retrofit2.HttpException
import java.io.IOException

object ErrorHandler {

    /**
     * Handles API/network exceptions
     * and displays corresponding toast messages.
     */
    fun handleException(
        context: Context,
        exception: Exception,
        unknownError: String?,
        networkError: String?
    ) {
        when(exception){
            is HttpException -> {
                val errorMessage =
                    ErrorResponse.parseError(
                        exception.response()
                            ?.errorBody()
                            ?.string()
                    )?.error ?: unknownError

                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is IOException -> {

                Toast.makeText(
                    context,
                    networkError,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                Toast.makeText(
                    context,
                    unknownError,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}