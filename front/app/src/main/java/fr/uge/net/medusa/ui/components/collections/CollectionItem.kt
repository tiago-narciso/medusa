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
import fr.uge.net.medusa.data.CollectionUi


/*

Outer Row
│
├── Left Row
│     ├── Collection Avatar
│     └── Column
│           ├── Collection Name
│           └── Power
│
└── Right Row
      ├── Card Count
      └── Arrow >

 */

@Composable
fun CollectionItem(

    index:Int,
    collection: CollectionUi,
    onClick: () -> Unit

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
                    .background(colors[index % colors.size]),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
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
                    text = collection.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Power : ${collection.power}",
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
                    text = "${collection.cardCount}",
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
fun CollectionItemPreview() {
    CollectionItem(
        0,
        CollectionUi("paris", 40, 23),
        onClick = {}
    )
}