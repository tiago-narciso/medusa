package fr.uge.net.medusa.ui.components.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.net.medusa.data.Card

@Composable
fun CardItem(
    card: Card,
    fibMultiplier: Int,
    onClick: (Card) -> Unit
){

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
            .clickable { onClick(card)}
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // makes elements inside centered vertically

    ) {
        //Left section
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Card avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
            }
            Spacer(modifier = Modifier.width(12.dp))
            /*
             * card info
             */
            Column {
                Text(
                    text = card.personality,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Power : ${card.power}",
                    style = MaterialTheme
                        .typography
                        .bodySmall
                )

                Text(
                    text = card.acquisitionDate,
                    style = MaterialTheme
                        .typography
                        .bodySmall
                )
            }
        }
        /*
         * Right row
         */
        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        MaterialTheme
                            .colorScheme
                            .primaryContainer
                            .copy(alpha = 0.7f)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = "✖$fibMultiplier",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme
                        .colorScheme
                        .onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ">",
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
            )
        }
    }

}