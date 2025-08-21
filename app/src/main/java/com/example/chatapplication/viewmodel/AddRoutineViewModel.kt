package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.chatapplication.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddRoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
): ViewModel() {
    private val _routineName = MutableStateFlow<String>("")
    val routineName: StateFlow<String> = _routineName

    private val _routineDesc = MutableStateFlow<String>("")
    val routineDesc: StateFlow<String> = _routineDesc

    fun onRoutineNameChange(name: String) {
        _routineName.value = name
    }

    fun onRoutineDescChange(desc: String) {
        _routineDesc.value = desc
    }
}