package com.example.chatapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.dto.RoutineDto
import com.example.chatapplication.data.repository.RoutineRepository
import com.example.chatapplication.domain.model.Exercise
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.Routine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutinesViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
): ViewModel() {
    private val _usersRoutines = MutableStateFlow<List<Routine>>(listOf())
    val usersRoutines: StateFlow<List<Routine>> = _usersRoutines

    private val _isEmpty = MutableStateFlow<Boolean>(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    init {
        viewModelScope.launch {
            _usersRoutines.collect {
                if (it.isEmpty()) {
                    _isEmpty.value = true
                } else {
                    _isEmpty.value = false
                }
            }
        }
        viewModelScope.launch {
            routineRepository.routineFlow.collect { routines ->
                Log.d("ROUTINEVIEWMODEL", "Collected routines: $routines")
                _usersRoutines.value = routines
            }
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            val response = routineRepository.deleteRoutine(routine)

            if (response is NetworkResult.Success) {
                _usersRoutines.value = routineRepository.getAllRoutines().data!!
            }
        }
    }
}