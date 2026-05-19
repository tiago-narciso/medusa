package fr.uge.net.medusa.activities


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import fr.uge.net.medusa.R
import fr.uge.net.medusa.api.ApiProvider
import fr.uge.net.medusa.data.*
import fr.uge.net.medusa.models.LoginRequest
import fr.uge.net.medusa.utils.ErrorHandler
import kotlinx.coroutines.launch
import kotlin.collections.emptyList


@Composable
@Preview
fun ProfileScreenActivity(
    modifier: Modifier = Modifier,
    onNavigateToRegister: () -> Unit = {},

) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // Initialize API service
    val apiService = ApiProvider.getMockApi();
    /*
     * Fake player information
     * TODO: retrieve from backend
     */
        val playerName = "Player Name"

        val playerPower = 1024

        val playerRank = 24

    var collections by remember {
        mutableStateOf<List<CollectionUi>>(emptyList())
    }
    val translations = mapOf(
        "network_error" to stringResource(R.string.error_network),
        "unknown_error" to stringResource(R.string.error_unknown),
    )
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isLoading = true;
        // Login POST request
        try {
            val cardsResponse = apiService.getCards()
            // Group cards by birthplace (our collection criteria)
            val groupedCards = cardsResponse.cards.groupBy { it.placeOfBirth }
            // Convert grouped cards into CollectionUi objects
            collections = groupedCards.map { elt ->
                val placeOfBirth  = elt.key
                val cards = elt.value
            CollectionUi(
                placeOfBirth,
                cards.size,
                cards.sumOf{card -> card.power}

            )}
        } catch (e: Exception) {
            ErrorHandler.handleException(context, e,
                translations["unknown_error"],
                translations["network_error"])

        } finally {
            isLoading = false
        }
    }
    Column(

        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),

        verticalArrangement =
            Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(20.dp))







}
}

