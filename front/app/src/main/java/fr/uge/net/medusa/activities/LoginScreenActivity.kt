package fr.uge.net.medusa.activities

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.net.medusa.R
import fr.uge.net.medusa.model.api.ApiProvider
import fr.uge.net.medusa.model.api.LoginResult
import fr.uge.net.medusa.ui.fields.Button
import fr.uge.net.medusa.ui.fields.StyledTextField
import kotlinx.coroutines.launch

@Composable
@Preview
fun LoginScreenActivity(
    modifier: Modifier = Modifier,
    onNavigateToRegister: () -> Unit = {},
    onAuthenticated: () -> Unit = {}
) {
    val context = LocalContext.current
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val api = ApiProvider.getApi()
    val translations = mapOf(
        "invalid_credentials" to stringResource(R.string.error_invalid_credentials),
        "network_error" to stringResource(R.string.error_network),
        "unknown_error" to stringResource(R.string.error_unknown),
        "login_button" to stringResource(R.string.login_button),
        "register_button" to stringResource(R.string.register_button),
        "login_screen_title" to stringResource(R.string.login_screen_title),
        "username_placeholder" to stringResource(R.string.username_placeholder),
        "password_placeholder" to stringResource(R.string.password_placeholder),
    )

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = translations["login_screen_title"]!!,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(64.dp))
        StyledTextField(
            value = login,
            onValueChange = {v -> login = v},
            placeholder = translations["username_placeholder"]!!,
        )
        Spacer(modifier = Modifier.height(24.dp))
        StyledTextField(
            value = password,
            onValueChange = {v -> password = v},
            placeholder = translations["password_placeholder"]!!,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            text = translations["login_button"]!!,
            disabled = login.isEmpty() || password.isEmpty(),
            isLoading = isLoading
        ) {
            coroutineScope.launch {
                isLoading = true;
                val result = api.login(login, password)
                when (result) {
                    is LoginResult.Success -> {
                        val token = result.authToken
                        // todo: save token, get user info and navigate to main activity
                        onAuthenticated();
                    }
                    is LoginResult.Error.InvalidCredentials -> {
                        Toast.makeText(context, translations["invalid_credentials"], Toast.LENGTH_SHORT).show()
                    }
                    is LoginResult.Error.NetworkError -> {
                        Toast.makeText(context, translations["network_error"], Toast.LENGTH_SHORT).show()
                    }
                    is LoginResult.Error.UnknownError -> {
                        Toast.makeText(context, translations["unknown_error"], Toast.LENGTH_SHORT).show()
                    }
                }
                isLoading = false
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            text = translations["register_button"]!!,
            onClick = onNavigateToRegister
        )
    }
}
