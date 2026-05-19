package fr.uge.net.medusa.activities


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
    var collections by remember {
        mutableStateOf<List<CollectionUi>>(emptyList())
    }
    var isLoading by remember { mutableStateOf(false) }
    coroutineScope.launch {
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
                cards.sumOf(it.power)

            )}
        } catch (e: Exception) {

        } finally {
            isLoading = false
        }
    }




}

