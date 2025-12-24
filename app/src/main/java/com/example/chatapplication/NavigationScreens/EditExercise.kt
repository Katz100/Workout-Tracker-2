package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.Components.CustomButton
import com.example.chatapplication.Components.CustomTextField
import com.example.chatapplication.viewmodel.EditExerciseViewModel

@Composable
fun EditExercise(
    modifier: Modifier = Modifier,
    viewModel: EditExerciseViewModel = hiltViewModel(),
) {
    val name = viewModel.exerciseName.collectAsState().value
    val desc = viewModel.exerciseDescription.collectAsState().value
    val sets = viewModel.exerciseSets.collectAsState().value
    val rest = viewModel.exerciseRest.collectAsState().value
    val reps = viewModel.exerciseReps.collectAsState().value

    Column(
        modifier = modifier
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            placeholder = "Exercise Name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = desc,
            onValueChange = { viewModel.onDescChange(it) },
            placeholder = "Description",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = sets,
            onValueChange = { viewModel.onSetsChange(it) },
            placeholder = "Number of sets",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = rest,
            onValueChange = { viewModel.onRestChange(it) },
            placeholder = "Rest Time (seconds)",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = reps,
            onValueChange = { viewModel.onRepsChange(it) },
            placeholder = "Reps per set (comma-separated, e.g. 12, 10, 8",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomButton(
                modifier = Modifier.weight(1f),
                onClick = { /** TODO: pop stack **/ },
                text = "Cancel",
                textModifier = Modifier.fillMaxWidth().padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            CustomButton(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.onSaveExercise() },
                text = "Save Exercise",
                textModifier = Modifier.fillMaxWidth().padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                buttonColor = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
            )
        }
    }
}