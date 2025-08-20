package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chatapplication.Nav.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NestedNavViewModel @Inject constructor(): ViewModel() {
    private val _topOfStack = MutableStateFlow<Boolean>(true)
    var topOfStack: StateFlow<Boolean> = _topOfStack

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Routine)
    val currentScreen: StateFlow<Screen> = _currentScreen

    private val _nestedScreenName = MutableStateFlow<String>("Screen Name")
    val nestedScreenName: StateFlow<String> = _nestedScreenName

    private val _showAddIcon = MutableStateFlow<Boolean>(true)
    val showAddIcon: StateFlow<Boolean> = _showAddIcon

    init {
        viewModelScope.launch {
            _currentScreen.collect { screen ->
                _showAddIcon.value = screen is Screen.Routine || screen is Screen.Exercise
            }
        }
    }

    fun setNestedScreenName(screenName: String) {
        _nestedScreenName.value = screenName
    }

    fun setScreen(screen: Screen) {
        Log.i("NAVVIEWMODEL", "Navigating to screen: $screen")
        _currentScreen.value = screen
    }

    fun setTopOfStack(navController: NavHostController) {
        _topOfStack.value = navController.previousBackStackEntry == null
    }
}