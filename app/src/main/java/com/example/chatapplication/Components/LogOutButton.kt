package com.example.chatapplication.Components

import android.widget.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LogOutButton(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit = {}
) {
    OutlinedButton(
        modifier = Modifier,
        onClick = onLogOut
    )  {
        Text(text = "Log Out")
    }

}

@Preview
@Composable
fun LogOutButtonPreview() {
    LogOutButton()
}
