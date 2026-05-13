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
import fr.uge.net.medusa.api.ApiClient
import fr.uge.net.medusa.api.ApiProvider
import fr.uge.net.medusa.models.*
import fr.uge.net.medusa.ui.fields.Button
import fr.uge.net.medusa.ui.fields.StyledTextField
import fr.uge.net.medusa.utils.ErrorHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@Composable
@Preview
fun RegisterScreenActivity(modifier: Modifier = Modifier, onAuthenticated: () -> Unit = {}) {
    val context = LocalContext.current
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val tokenStore = remember(context) { TokenStore(context.applicationContext) }
    var isLoading by remember { mutableStateOf(false) }
    // Initialize API service
    val apiService = ApiProvider.getRealApi();
    val coroutineScope = rememberCoroutineScope()
    val translations = mapOf(
        "register_screen_title" to stringResource(R.string.register_screen_title),
        "username_placeholder" to stringResource(R.string.username_placeholder),
        "password_placeholder" to stringResource(R.string.password_placeholder),
        "register_button" to stringResource(R.string.register_button),
        "network_error" to stringResource(R.string.error_network),
        "unknown_error" to stringResource(R.string.error_unknown),
        )
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = translations["register_screen_title"]!!,
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
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            text = translations["register_button"]!!,
            disabled = login.isEmpty() || password.isEmpty(),
            isLoading = isLoading
        ) {
            coroutineScope.launch {
                isLoading = true;
                // Register POST request
                try {
                    val registerResponse = apiService.register(
                        RegisterRequest(login, password)
                    )
                    // todo: save token, get user info and navigate to main activity
                    // if token not null execute the block
                    val token = registerResponse.token
                    tokenStore.saveToken(token)
                    onAuthenticated()
                }catch(e:Exception){
                    ErrorHandler.handleException(context, e,
                        translations["unknown_error"],
                        translations["network_error"])
                }finally {
                    isLoading = false
                }
            }
        }
        }
    }

