package com.example.chatapplication.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.data.repository.RoutineRepository
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

    @Deprecated("Remove in favor of _isRefreshing")
    private val _isEmpty = MutableStateFlow<Boolean>(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    private val _isRefreshing = MutableStateFlow<Boolean>(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        viewModelScope.launch {
            _usersRoutines.collect {
                _isEmpty.value = it.isEmpty()
            }
        }

        viewModelScope.launch {
            val response = routineRepository.getAllRoutines()

            if (response !is NetworkResult.Error && response.data != null) {
                _usersRoutines.value = response.data
            }
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            val response = routineRepository.deleteRoutine(routine)

            if (response is NetworkResult.Success) {
                val routines = routineRepository.getAllRoutines().data

                if (routines != null) {
                    _usersRoutines.value = routines
                }
            }
        }
    }

    fun refreshRoutines() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val response = routineRepository.getAllRoutines()

            if (response !is NetworkResult.Error) {
                val routines = response.data
                if (routines != null) {
                    _usersRoutines.value = routines
                }
            }
            _isRefreshing.value = false
        }
    }
}