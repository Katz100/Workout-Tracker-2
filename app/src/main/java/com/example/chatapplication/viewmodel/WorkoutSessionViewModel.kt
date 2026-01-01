package com.example.chatapplication.viewmodel

import com.example.chatapplication.R
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.chatapplication.Nav.Screen
import com.example.chatapplication.util.MyReceiver
import com.example.chatapplication.util.Timer
import com.example.chatapplication.util.TimerService
import com.example.chatapplication.data.repository.UsersRoutineExercisesRepository
import com.example.chatapplication.domain.model.NetworkResult
import com.example.chatapplication.domain.model.RoutineExercise
import com.example.chatapplication.domain.model.UsersRoutineExercises
import com.example.chatapplication.util.TimerEvent
import com.example.chatapplication.util.WorkoutTrackingService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
    private val usersRoutineExercisesRepository: UsersRoutineExercisesRepository,
    private val workoutTrackingService: WorkoutTrackingService,
) : ViewModel() {

    private val routineArg = savedStateHandle.toRoute<Screen.WorkoutSession>()

    private val _exerciseIndex = MutableStateFlow(0)
    val exerciseIndex: StateFlow<Int> = _exerciseIndex

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val restTime: StateFlow<Int> = Timer.restTime

    private val _usersExercises = MutableStateFlow<List<UsersRoutineExercises>>(listOf())
    val usersExercises: StateFlow<List<UsersRoutineExercises>> = _usersExercises

    val isResting: StateFlow<Boolean> = Timer.isResting

    private val _currentSet = MutableStateFlow<Int>(1)
    val currentSet: StateFlow<Int> = _currentSet

    private val _currentExercise = MutableStateFlow<UsersRoutineExercises>(
        UsersRoutineExercises(
            exerciseName = "Empty",
            routineName = "",
            exerciseId = "",
            sets = 1,
            reps = listOf(12, 8, 6),
            rest = 120,
            orderIndex = 0
        )
    )
    val currentExercise: StateFlow<UsersRoutineExercises> = _currentExercise

    private var mediaPlayer: MediaPlayer? = null

    private val _workoutFinished = MutableStateFlow<Boolean>(false)
    val workoutFinished: StateFlow<Boolean> = _workoutFinished

    init {
        loadRoutine(routineArg.routineId)
        workoutTrackingService.startSession()
        viewModelScope.launch {
            Timer.timerEventChannel.receiveAsFlow().collect { event ->
                Log.i("TimerService", "From vm, collect: $event")
                when (event) {
                    TimerEvent.OnStopTimer -> stopTimer()
                    TimerEvent.OnTimerFinished -> {
                        playSound()
                        onNextSet()
                        stopTimer()
                    }
                }
            }
        }
    }

    fun nextExercise(): String {
        return if (_exerciseIndex.value >= _usersExercises.value.size - 1) "N/A" else _usersExercises.value[_exerciseIndex.value + 1].exerciseName
    }

    fun previousExercise(): String {
        return if (_exerciseIndex.value == 0) "N/A" else _usersExercises.value[_exerciseIndex.value - 1].exerciseName
    }

    fun playSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.beep2)
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    fun stopTimer() {
        val intent = Intent(context, TimerService::class.java)
        context.stopService(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startTimer() {
        // Moves timer logic into foreground service so operations
        // survive while app is in the background
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra("duration", _currentExercise.value.rest)
        }
        context.startForegroundService(intent)
    }

    fun formatDuration(seconds: Int): String {
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return "%02d:%02d".format(minutes, secs)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun loadRoutine(routineId: String) {
        viewModelScope.launch {
            val result = usersRoutineExercisesRepository.getUsersExercisesByRoutine(routineId)
            if (result !is NetworkResult.Error) {
                _usersExercises.value = result.data ?: emptyList()
                if (_usersExercises.value.isNotEmpty()) {
                    _currentExercise.value = _usersExercises.value[_exerciseIndex.value]
                }
                _isLoading.value = false
            }
        }
    }

    fun onPreviousSet() {
        stopTimer()

        if (usersExercises.value.isEmpty()) return

        _currentSet.value--
        if (_currentSet.value == 0) {
            if (_exerciseIndex.value != 0) {
                _exerciseIndex.value--
                _currentExercise.value = usersExercises.value[_exerciseIndex.value]
                _currentSet.value = _currentExercise.value.sets
            } else if (_exerciseIndex.value == 0 && _currentSet.value == 0) {
                _currentSet.value = 1
            } else {
                _currentSet.value = _currentExercise.value.sets
            }
        }
    }

    fun onNextSet() {
        stopTimer()

        val list = _usersExercises.value
        if (list.isEmpty()) return

        val cur = _currentExercise.value
        _currentSet.value++

        if (_currentSet.value > cur.sets) {
            _currentSet.value = 1
            val nextIndex = _exerciseIndex.value + 1

            if (nextIndex < list.size) {
                _exerciseIndex.value = nextIndex
                _currentExercise.value = list[nextIndex]
            } else {
                workoutTrackingService.endSession()
                _workoutFinished.value = true
                Log.i("WorkoutSessionVM", "Workout finished")
                // temporary toast
                Toast.makeText(context, "Time spent workout out: ${workoutTrackingService.getTotalDurationMinutes()}", Toast.LENGTH_SHORT).show()
                workoutTrackingService.reset()
            }
        }
    }

    fun skipToExercise(exercise: UsersRoutineExercises) {
        if (exercise.orderIndex > _exerciseIndex.value) {
            while (_currentExercise.value !== exercise) {
                onNextSet()
            }
        } else {
            while (_currentExercise.value !== exercise) {
                onPreviousSet()
            }
            _currentSet.value = 1
        }
    }
}
