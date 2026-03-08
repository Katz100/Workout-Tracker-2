package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NestedNavViewModel @Inject constructor(): ViewModel() {
    companion object {
        const val TAG = "NestedNavViewModel"
    }

    private val _shouldRefreshRoutines = MutableStateFlow<Boolean>(false)
    val shouldRefreshRoutines: StateFlow<Boolean> = _shouldRefreshRoutines

    private val _currentDestination = MutableStateFlow<NavDestination?>(null)
    val currentDestination: StateFlow<NavDestination?> = _currentDestination

    private val _currentRoute = MutableStateFlow<String?>(null)
    val currentRoute: StateFlow<String?> = _currentRoute

    private val _isTopLevel = MutableStateFlow<Boolean>(true)
    val isTopLevel: StateFlow<Boolean> = _isTopLevel

    private val _screenTitle = MutableStateFlow<String>("")
    val screenTitle: StateFlow<String> = _screenTitle

    private val _openAlertDialog = MutableStateFlow<Boolean>(false)
    val openAlertDialog: StateFlow<Boolean> = _openAlertDialog

    fun setShouldRefreshRoutines(value: Boolean) {
        _shouldRefreshRoutines.value = value
    }

    fun onRouteChanged(value: String?) {
        if (value != null) {
            Log.i(TAG, "Route changed: $value")
        }
    }
}