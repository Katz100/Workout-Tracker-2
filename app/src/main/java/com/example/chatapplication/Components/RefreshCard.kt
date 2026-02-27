package com.example.chatapplication.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RefreshCard(
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F291E)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFF10B981)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "List Icon",
                modifier = Modifier.padding(end = 4.dp),
                tint = Color.Green
            )

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Update available for your routines.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
                Text(
                    text = "Please refresh to see the latest changes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFB7D6CE),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RefreshCardPreview(
) {
    RefreshCard()
}