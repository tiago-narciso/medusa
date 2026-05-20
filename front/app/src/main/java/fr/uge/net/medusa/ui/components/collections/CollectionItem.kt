package fr.uge.net.medusa.ui.components.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CollectionItem(

    collectionName: String,
    numberOfCards : Int,
    power: Int,
    collectionNumber: Int
) {
    val colors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF5DADE2),
        Color(0xFFF5B041),
        Color(0xFFF7DC6F)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // makes rank be on the left
        verticalAlignment = Alignment.CenterVertically // makes elements inside centered vertically

    ) {
        //Left section
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Collection avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors[collectionNumber % colors.size]),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$collectionNumber",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            /*
             * Player info
             */
            Column {
                Text(
                    text = collectionName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Power : $power",
                    style = MaterialTheme
                        .typography
                        .bodySmall
                )
            }
        }
        /*
         * Number of cards
         */
        Box(
            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                .background(
                    MaterialTheme
                        .colorScheme
                        .primaryContainer
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                )
        ) {
            Text(
                text = "$numberOfCards cards",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme
                        .colorScheme
                        .onPrimaryContainer
            )
        }
    }
}

@Composable
@Preview
fun CollectionItemPreview() {
    CollectionItem(
        collectionName = "Paris",
        numberOfCards = 50,
        power = 100,
        2
    )
}