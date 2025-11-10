package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.chatapplication.viewmodel.EditExerciseViewModel

@Composable
fun EditExercise(
    modifier: Modifier = Modifier,
    viewModel: EditExerciseViewModel = hiltViewModel(),
) {
    val currExercise = viewModel.exerciseToEdit.collectAsState().value

    Column(
        modifier = modifier
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = currExercise.name,
            onValueChange = {  },
            placeholder = "Exercise Name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = currExercise.description,
            onValueChange = {  },
            placeholder = "Description",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = currExercise.sets.toString(),
            onValueChange = {   },
            placeholder = "Number of sets",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = currExercise.rest.toString(),
            onValueChange = {  },
            placeholder = "Rest Time (seconds)",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = currExercise.reps.toString(),
            onValueChange = {  },
            placeholder = "Reps per set (comma-separated, e.g. 12, 10, 8",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}