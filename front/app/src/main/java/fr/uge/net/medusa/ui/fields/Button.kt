package fr.uge.net.medusa.ui.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val buttonShape = RoundedCornerShape(16.dp)
private val enabledButtonBackground = Color(0xFF3F7BFA)
private val disabledButtonBackground = Color(0xFF939393)

@Composable
fun Button(
    text: String,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    MaterialButton(
        onClick = onClick,
        enabled = !disabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 16.dp),
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledButtonBackground,
            disabledContainerColor = disabledButtonBackground,
            contentColor = Color.White,
            disabledContentColor = Color.White,
        ),
    ) {
        if (isLoading) {
            Text(
                text = "Loading...",
                fontSize = 22.sp,
            )
        } else {
            Text(
                text = text,
                fontSize = 22.sp,
            )
        }
    }
}


@Preview
@Composable
private fun ButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp),
    ) {
        Button("Login") {}
        Button("Register", disabled = true) {}
    }
}