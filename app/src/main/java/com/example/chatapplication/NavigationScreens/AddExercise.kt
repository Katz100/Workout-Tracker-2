package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapplication.Components.CustomButton
import com.example.chatapplication.Components.CustomTextField
import com.example.chatapplication.viewmodel.AddExerciseViewModel

@Composable
fun AddExercise(
    modifier: Modifier = Modifier,
    addExerciseViewModel: AddExerciseViewModel = hiltViewModel(),

) {
    val exerciseName = addExerciseViewModel.exerciseName.collectAsState().value
    val desc = addExerciseViewModel.exerciseDescription.collectAsState().value
    val sets = addExerciseViewModel.sets.collectAsState().value
    val rest = addExerciseViewModel.rest.collectAsState().value
    val repsPerSet = addExerciseViewModel.repsPerSet.collectAsState().value

    Column(
        modifier = modifier
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = exerciseName,
            onValueChange = { addExerciseViewModel.onExerciseNameChange(it) },
            placeholder = "Exercise Name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = desc,
            onValueChange = { addExerciseViewModel.onExerciseDescriptionChange(it) },
            placeholder = "Description",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = sets,
            onValueChange = { addExerciseViewModel.onSetsChange(it)  },
            placeholder = "Number of sets",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = rest,
            onValueChange = { addExerciseViewModel.onRestChange(it) },
            placeholder = "Rest Time (seconds)",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = repsPerSet,
            onValueChange = { addExerciseViewModel.onRepsPerSetChange(it) },
            placeholder = "Reps per set (comma-separated, e.g. 12, 10, 8",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp),
            onClick = { addExerciseViewModel.onAddExerciseButtonClicked() },
            text = "Add",
            textModifier = Modifier.fillMaxWidth().padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
    }
}