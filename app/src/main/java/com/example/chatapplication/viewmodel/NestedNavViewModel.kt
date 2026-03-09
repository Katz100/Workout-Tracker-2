package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
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

    fun setShouldRefreshRoutines(value: Boolean) {
        _shouldRefreshRoutines.value = value
    }
}
