package fr.uge.net.medusa.activities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.net.medusa.data.Card
import fr.uge.net.medusa.data.CardsCollection
import fr.uge.net.medusa.ui.components.collections.CardItem
import fr.uge.net.medusa.ui.components.navigation.BottomNavigationBar

@Composable
fun CollectionActivity(

    collection: CardsCollection,
    currentRoute: String,
    innerPadding: PaddingValues,
    onNavigate: (String) -> Unit,
    onNavigateToCard: (Card) -> Unit,
    onBack:() -> Unit
) {

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
             * Header
             */
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    modifier = Modifier.clickable{onBack()}
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = collection.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            /*
             * Total collection score
             */
            Text(
                text = "Total Score : ${collection.power}",
                style = MaterialTheme
                        .typography
                        .bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            /*
             * Cards list
             */
            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {

                itemsIndexed(
                    collection.cards
                ) { index, card ->
                    val fibMultiplier =
                        fibonacci(index + 1)
                    CardItem(
                        card = card,
                        fibMultiplier = fibMultiplier,
                        onClick = {
                            onNavigateToCard(card)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        /*
     * Bottom navigation bar
     */
        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter),
            innerPadding
        )
    }
}