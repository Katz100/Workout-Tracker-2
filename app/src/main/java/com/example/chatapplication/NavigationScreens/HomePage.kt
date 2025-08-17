package com.example.chatapplication.NavigationScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapplication.Components.LogOutButton

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LogOutButton(
            onLogOut = onLogOut
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage(onLogOut = {})
}