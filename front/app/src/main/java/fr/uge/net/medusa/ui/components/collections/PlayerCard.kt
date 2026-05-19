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

/*
Outer Row
│
├── Left Row
│     ├── Avatar
│     └── Column
│           ├── Name
│           └── Power
│
└── Rank Text

 */


@Composable
fun PlayerCard(

    playerName: String,

    playerPower: Int,

    playerRank: Int
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
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // makes rank be on the left
        verticalAlignment = Alignment.CenterVertically // makes elements inside centered vertically

    ) {
        //Left section
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            /*
             * Player info
             */
            Column {
                Text(
                    text = playerName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Power : $playerPower",
                    style = MaterialTheme
                            .typography
                            .bodySmall
                )
            }
        }
        /*
         * Rank
         */
        Text(
            text = "#$playerRank",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
@Preview
fun PlayerCardPreview(){
    PlayerCard(
        playerName = "narmin",
        playerPower = 1000,
        playerRank = 24
    )
}