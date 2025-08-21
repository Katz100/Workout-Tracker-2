package com.example.chatapplication.NavigationScreens

import android.util.Log
import androidx.compose.foundation.layout.Column
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
import com.example.chatapplication.data.dto.RoutineDto
import com.example.chatapplication.viewmodel.AddRoutineViewModel

@Composable
fun AddRoutine(
    modifier: Modifier = Modifier,
    addRoutineViewModel: AddRoutineViewModel = hiltViewModel(),
    onNextButtonClicked: (String, String) -> Unit,
) {
    val routineName = addRoutineViewModel.routineName.collectAsState().value
    val routineDesc = addRoutineViewModel.routineDesc.collectAsState().value

    Column(
        modifier = modifier
    ) {
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            value = routineName,
            onValueChange = { addRoutineViewModel.onRoutineNameChange(it) },
            placeholder = "Routine Name",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
                .height(100.dp),
            value = routineDesc,
            onValueChange = { addRoutineViewModel.onRoutineDescChange(it) },
            placeholder = "Routine Description",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
        )

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 25.dp, 0.dp, 0.dp),
            onClick = {

                Log.d("AddRoutine", "In progress of saving routine: $routineName, $routineDesc")
                onNextButtonClicked(routineName, routineDesc)
            },
            text = "Next",
            textModifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
    }
}