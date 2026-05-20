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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/*
Outer Row
│
├── Left Row
│     ├──  Avatar
│     └── name
│
└── Right Row
      ├── Card Count
      └── Arrow >

 */

@Composable
fun allCardsItem(

    nbCards:Int,
    onClick: () -> Unit

) {

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
            .clickable { onClick() }
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
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A-Z",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            /*
             * Player info
             */
            Text(
                text = "All cards",
                fontWeight = FontWeight.Bold
            )

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
                    text = "$nbCards",
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
@Composable
@Preview
fun AllCardsItemPreview(){
    allCardsItem (
        50,
        {}
    )
}
