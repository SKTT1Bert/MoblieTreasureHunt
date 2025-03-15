/*
 * Assignment 6
 * TreasureHuntViewModel.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * Oregon State University
 * CS 492
 */

package com.example.treasurehunt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.treasurehunt.data.TreasureRepository
import com.example.treasurehunt.location.LocationService
import com.example.treasurehunt.model.Clue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.location.Location

class TreasureHuntViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TreasureRepository()

    private val _clues = MutableStateFlow<List<Clue>>(emptyList())
    val clues = _clues.asStateFlow()

    private val _currentClueIndex = MutableStateFlow(0)
    val currentClueIndex = _currentClueIndex.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning = _isTimerRunning.asStateFlow()

    private val _userLocation = MutableStateFlow(Location("").apply {
        latitude = 0.0
        longitude = 0.0
    })
    val userLocation = _userLocation.asStateFlow()

    init {
        loadClues()
    }

    private fun loadClues() {
        val list = repository.loadClues()
        _clues.value = list
    }

    fun startGame() {
        _currentClueIndex.value = 0
        _elapsedTime.value = 0L
        _isTimerRunning.value = true
        startTimer()
    }

    fun quitGame() {
        // Reset everything
        _isTimerRunning.value = false
        _elapsedTime.value = 0L
        _currentClueIndex.value = 0
    }

    private fun startTimer() {
        // Launch a coroutine that increments time every second while isTimerRunning is true
        viewModelScope.launch {
            while (_isTimerRunning.value) {
                delay(1000)
                _elapsedTime.value = _elapsedTime.value + 1
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
    }

    fun resumeTimer() {
        // If the game isn't finished, we can resume
        if (_currentClueIndex.value < _clues.value.size) {
            _isTimerRunning.value = true
            startTimer()
        }
    }

    // Called when user taps "Found It!"
    fun checkLocation(): Boolean {
        val clue = getCurrentClue() ?: return false
        val userLat = _userLocation.value.latitude
        val userLng = _userLocation.value.longitude

        return LocationService.isUserAtLocation(
            userLat = userLat,
            userLng = userLng,
            targetLat = clue.latitude,
            targetLng = clue.longitude,
            thresholdMeters = 30.0
        )
    }

    fun getCurrentClue(): Clue? {
        return if (_currentClueIndex.value in _clues.value.indices) {
            _clues.value[_currentClueIndex.value]
        } else null
    }

    fun goToNextClue() {
        if (_currentClueIndex.value < _clues.value.size - 1) {
            _currentClueIndex.value += 1
        }
    }

    fun isFinalClue(): Boolean {
        val clue = getCurrentClue() ?: return false
        return clue.isFinal
    }

    // Called whenever the system or UI obtains a new user location
    fun updateUserLocation(lat: Double, lng: Double) {
        val loc = Location("").apply {
            latitude = lat
            longitude = lng
        }
        _userLocation.value = loc
    }
}
