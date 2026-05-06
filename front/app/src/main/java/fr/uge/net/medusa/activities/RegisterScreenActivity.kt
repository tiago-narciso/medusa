package fr.uge.net.medusa.activities

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.net.medusa.R
import fr.uge.net.medusa.ui.fields.Button
import fr.uge.net.medusa.ui.fields.StyledTextField

@Composable
@Preview
fun RegisterScreenActivity(modifier: Modifier = Modifier, onAuthenticated: () -> Unit = {}) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.register_screen_title),
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(64.dp))
        StyledTextField(
            value = login,
            onValueChange = {v -> login = v},
            placeholder = stringResource(R.string.username_placeholder),
        )
        Spacer(modifier = Modifier.height(24.dp))
        StyledTextField(
            value = password,
            onValueChange = {v -> password = v},
            placeholder = stringResource(R.string.password_placeholder),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            text = stringResource(R.string.register_button),
            disabled = login.isEmpty() || password.isEmpty()
        ) {
            // TODO: register
        }
    }
}
