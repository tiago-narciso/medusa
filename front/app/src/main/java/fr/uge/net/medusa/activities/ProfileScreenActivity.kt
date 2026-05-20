package fr.uge.net.medusa.activities


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.uge.net.medusa.R
import fr.uge.net.medusa.api.ApiProvider
import fr.uge.net.medusa.data.*
import fr.uge.net.medusa.models.LoginRequest
import fr.uge.net.medusa.navigation.Routes
import fr.uge.net.medusa.ui.components.collections.CollectionItem
import fr.uge.net.medusa.ui.components.collections.PlayerItem
import fr.uge.net.medusa.ui.components.navigation.BottomNavigationBar
import fr.uge.net.medusa.utils.ErrorHandler
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

fun fibonacci(num: Int): Int {
    var a = 0
    var b = 1
    var tmp: Int
    for (i in 2..num) {
        tmp = a + b
        a = b
        b = tmp
    }
    return b
}

@Composable
fun ProfileScreenActivity(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    innerPadding: PaddingValues,

    ) {
    val context = LocalContext.current
    // Initialize API service
    val apiService = ApiProvider.getMockApi();

    var collections by remember {
        mutableStateOf<List<CollectionUi>>(emptyList())
    }
    var playerPower by remember { mutableIntStateOf(0) }
    val translations = mapOf(
        "network_error" to stringResource(R.string.error_network),
        "unknown_error" to stringResource(R.string.error_unknown),
    )
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isLoading = true;
        try {
            val cardsResponse = apiService.getCards()
            //val totalCards = cardsResponse.cards.size
            playerPower = cardsResponse.cards.sumOf { card -> card.power }
            // Group cards by birthplace (our collection criteria)
            val groupedCards = cardsResponse.cards.groupBy { it.placeOfBirth }
            // Convert grouped cards into CollectionUi objects
            collections = groupedCards.map { elt ->
                val placeOfBirth = elt.key
                val cards = elt.value.sortedBy {
                    it.acquisitionDate
                }
                // fibonacci multiplier
                val collectionPower = cards.mapIndexed{index, card->
                    val multiplier = fibonacci(index + 1)
                    card.power * multiplier
                }.sum()
                CollectionUi(
                    placeOfBirth,
                    cards.size,
                    collectionPower

                )
            }
        } catch (e: Exception) {
            ErrorHandler.handleException(
                context, e,
                translations["unknown_error"],
                translations["network_error"]
            )

        } finally {
            isLoading = false
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            /*
             * Player profile Item
             */
            PlayerItem(
                // TODO : get player login
                playerName = "Player",
                playerPower = playerPower,
            )
            Spacer(modifier = Modifier.height(16.dp))
            /*
             * Collections list
             */
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    itemsIndexed(
                        collections
                    ) { index, collection ->
                        CollectionItem(
                            index = index,
                            collection = collection,
                            onClick = {
                                onNavigate(
                                    collection.name
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
            /*
         * Bottom navigation bar
         */
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { },
                modifier = Modifier.align(Alignment.BottomCenter),
                innerPadding
            )
        }

    }


@Preview(showBackground = true)
@Composable
fun PreviewProfileScreenActivity() {

    val fakeCollections = listOf(

        CollectionUi(
            "Paris",
            40,
            120,
        ),

        CollectionUi(
            "Tokyo",
            12,
            80
        ),

        CollectionUi(
            "Rome",
            8,
            45
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            PlayerItem(

                playerName = "Nermine",

                playerPower = 245
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            LazyColumn {

                itemsIndexed(
                    fakeCollections
                ) { index, collection ->

                    CollectionItem(

                        index = index,

                        collection = collection,

                        onClick = {}
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )
                }
            }
        }
        /*
  * Bottom navigation bar
  */
        BottomNavigationBar(
            currentRoute = Routes.PROFILE,
            onNavigate = {  },
            modifier = Modifier.align(Alignment.BottomCenter),
            innerPadding = PaddingValues()
        )
    }
}









