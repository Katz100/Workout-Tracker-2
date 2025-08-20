package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
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
    Column(
        modifier = modifier
    ) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = "exercise",
            onValueChange = {  },
            placeholder = "Exercise Name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = "exercise",
            onValueChange = {  },
            placeholder = "Description",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = "exercise",
            onValueChange = {  },
            placeholder = "Number of sets",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = "exercise",
            onValueChange = {  },
            placeholder = "Rest Time (seconds)",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = "exercise",
            onValueChange = {  },
            placeholder = "Reps per set (comma-separated, e.g. 12, 10, 8",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp),
            onClick = {},
            text = "Add",
            textModifier = Modifier.fillMaxWidth().padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
    }
}