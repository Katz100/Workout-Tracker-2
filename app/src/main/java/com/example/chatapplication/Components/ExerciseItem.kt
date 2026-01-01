package com.example.chatapplication.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier,
    current: Boolean = false,
    sets: Int,
    exerciseName: String,
    onExerciseSelected: () -> Unit,
) {
    if (!current) {
        Row(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = exerciseName,
                    color = Color.Gray
                )
                Text(
                    text = "$sets sets",
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onExerciseSelected,
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "arrow"
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .background(color = Color(0xFF233A4D), shape = RoundedCornerShape(12.dp))
                .border(width = 1.dp, color = Color(0xFF4052DD), shape = RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = exerciseName,
                    color = Color.Gray
                )
                Text(
                    text = "Current",
                    color = Color(0xFF4052DD)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onExerciseSelected,
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "arrow"
                )
            }
        }
    }
}

@Preview
@Composable
fun ExerciseItemPreview() {
    ExerciseItem(
        modifier = Modifier.width(150.dp),
        sets = 3,
        exerciseName = "Push ups",
        onExerciseSelected = {}
    )
}

@Preview
@Composable
fun ExerciseItemCurrentPreview() {
    ExerciseItem(
        modifier = Modifier.width(150.dp),
        sets = 3,
        exerciseName = "Push ups",
        onExerciseSelected = {},
        current = true
    )
}