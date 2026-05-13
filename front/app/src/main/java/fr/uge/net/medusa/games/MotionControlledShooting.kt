package fr.uge.net.medusa.games

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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

}

@Composable
@Preview
fun MotionControlledShooting(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()

) {
    val context = LocalContext.current




    fun loadImageFromAssets(context: Context, path: String): ImageBitmap? {
        val assetManager = context.assets
        val istr = assetManager.open(path)
        val bitmap = BitmapFactory.decodeStream(istr)
        istr.close()
        return bitmap.asImageBitmap()
    }

    @Composable
    fun getAssetImage(path: String): ImageBitmap? {
        val context = LocalContext.current
        var bitmap by remember { mutableStateOf<ImageBitmap?>(null) } // we keep this bitmap in cache
        LaunchedEffect(path) {
            bitmap = loadImageFromAssets(context, path)
        }
        return bitmap
    }

    @Composable
    fun FishDisplayer(fish: Shooters, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
        Box(modifier) {
            val path = "fishes/" + fish.filename
            getAssetImage(path)?.let { Image(it, "") }
            // in case it's null ?. only calls when result is non nullable

        }
    }

}

