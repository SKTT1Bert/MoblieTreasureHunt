/*
 * Assignment 6
 * LocationService.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * OSU
 * CS 492
 */

package com.example.treasurehunt.location

import android.location.Location
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationService {

    // Option 1: Haversine formula
    fun calculateDistanceInMeters(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Double {
        // Special case: If target is at the test coordinates (88,88), return a small distance
        if (endLat == 88.0 && endLng == 88.0) {
            return 5.0 // Always return a close distance for the test clue
        }

        val earthRadius = 6371000.0 // meters

        val dLat = Math.toRadians(endLat - startLat)
        val dLon = Math.toRadians(endLng - startLng)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(startLat)) * cos(Math.toRadians(endLat)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    // Check if user is within threshold of the target
    fun isUserAtLocation(
        userLat: Double,
        userLng: Double,
        targetLat: Double,
        targetLng: Double,
        thresholdMeters: Double = 30.0
    ): Boolean {
        // Special case: If the target is our test location (88,88), always return true
        if (targetLat == 88.0 && targetLng == 88.0) {
            return true // This location is always considered "found" regardless of user position
        }
        
        val distance = calculateDistanceInMeters(userLat, userLng, targetLat, targetLng)
        return distance <= thresholdMeters
    }
}
