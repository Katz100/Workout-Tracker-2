package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NestedNavViewModel @Inject constructor(): ViewModel() {
    private val _topOfStack = MutableStateFlow<Boolean>(true)
    var topOfStack: StateFlow<Boolean> = _topOfStack

    private val _nestedScreenName = MutableStateFlow<String>("Screen Name")
    val nestedScreenName: StateFlow<String> = _nestedScreenName

    fun setTopOfStack(value: Boolean) {
        _topOfStack.value = value
    }

    fun setNestedScreenName(name: String) {
        _nestedScreenName.value = name
    }
}