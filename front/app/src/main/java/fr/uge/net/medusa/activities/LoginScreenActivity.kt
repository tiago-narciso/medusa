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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.uge.net.medusa.R
import fr.uge.net.medusa.models.LoginRequest
import fr.uge.net.medusa.models.TokenStore
import fr.uge.net.medusa.api.ApiClient
import fr.uge.net.medusa.api.ApiProvider
import fr.uge.net.medusa.models.ErrorResponse
import fr.uge.net.medusa.ui.fields.Button
import fr.uge.net.medusa.ui.fields.StyledTextField
import fr.uge.net.medusa.utils.ErrorHandler
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class LoginViewModel : ViewModel() {
    var login by  mutableStateOf("")
    var password by  mutableStateOf("")
    var isLoading by mutableStateOf(false)
}

@Composable
@Preview
fun LoginScreenActivity(
    modifier: Modifier = Modifier,
    onNavigateToRegister: () -> Unit = {},
    onAuthenticated: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()

) {
    val context = LocalContext.current
    val tokenStore = remember(context) { TokenStore(context.applicationContext) }
    //var login by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }
    //var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    // Initialize API service
    val apiService = ApiProvider.getMockApi();
    val translations = mapOf(
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
            value = viewModel.login,
            onValueChange = { v -> viewModel.login = v },
            placeholder = translations["username_placeholder"]!!,
        )
        Spacer(
            modifier
            = Modifier.height(24.dp)
        )
        StyledTextField(
            value = viewModel.password,
            onValueChange = { v -> viewModel.password = v },
            placeholder = translations["password_placeholder"]!!,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            text = translations["login_button"]!!,
            disabled = viewModel.login.isEmpty() || viewModel.password.isEmpty(),
            isLoading = viewModel.isLoading
        ) {
            coroutineScope.launch {
                viewModel.isLoading = true;
                // Login POST request
                try {
                    val loginResponse = apiService.login(
                        LoginRequest(viewModel.login, viewModel.password)
                    )
                    // todo: save token, get user info and navigate to main activity
                    // if token not null execute the block
                    val token = loginResponse.token
                    tokenStore.saveToken(token)
                    onAuthenticated()
                } catch (e: Exception) {
                    ErrorHandler.handleException(context, e,
                        translations["unknown_error"],
                        translations["network_error"])

                } finally {
                    viewModel.isLoading = false
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            text = translations["register_button"]!!,
            onClick = onNavigateToRegister
        )
    }
}

