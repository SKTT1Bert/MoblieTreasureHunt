/*
 * Assignment 6
 * Clue.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * OSU
 * CS 492
 */

package com.example.treasurehunt.model

import kotlinx.serialization.Serializable

@Serializable
data class Clue(
    val id: Int,
    val text: String,
    val hint: String,
    val latitude: Double,
    val longitude: Double,
    val info: String,
    val isFinal: Boolean
)
