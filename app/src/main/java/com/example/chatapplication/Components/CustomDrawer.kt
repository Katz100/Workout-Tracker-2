package com.example.chatapplication.Components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomDrawer(
    contacts: List<String> = emptyList()
) {
    val state = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                LazyColumn(
                    state = state,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(contacts.size) { index ->
                        Text(contacts[index])
                    }
                }
            }
        }
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun CustomDrawerPreview(
    contacts: List<String> = listOf("Cody", "John", "Ben")
) {}