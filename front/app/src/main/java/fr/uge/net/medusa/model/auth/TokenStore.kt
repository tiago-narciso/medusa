package fr.uge.net.medusa.model.auth

import android.content.Context
import androidx.core.content.edit

class TokenStore(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit {
            putString(KEY_AUTH_TOKEN, token)
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun clearToken() {
        sharedPreferences.edit {
            remove(KEY_AUTH_TOKEN)
        }
    }

    companion object {
        private const val PREFS_NAME = "medusa_auth_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
    }
}
