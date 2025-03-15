/*
 * Assignment 6
 * ClueScreen.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * OSU
 * CS 492
 */

package com.example.treasurehunt.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.R
import com.example.treasurehunt.TreasureHuntViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import android.content.Context
import android.location.LocationManager
import com.example.treasurehunt.location.LocationService

@Composable
fun ClueScreen(
    viewModel: TreasureHuntViewModel,
    onFoundIt: (Boolean) -> Unit,
    onQuit: () -> Unit,
    fusedLocationClient: FusedLocationProviderClient = androidx.compose.ui.platform.LocalContext.current
        .getSystemService(Context.LOCATION_SERVICE) as FusedLocationProviderClient
) {
    val currentClue = viewModel.getCurrentClue()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    // For toggling hint
    var showHint by remember { mutableStateOf(false) }
    
    // Show location update message
    var showLocationMessage by remember { mutableStateOf(false) }
    var locationMessage by remember { mutableStateOf("") }
    
    // Show incorrect location dialog
    var showIncorrectLocationDialog by remember { mutableStateOf(false) }
    
    // Pre-load string resources to avoid calling them in non-Composable context
    val locationUpdatedMessage = stringResource(R.string.location_updated)
    val locationErrorMessage = stringResource(R.string.location_error)
    val lastLocationUsedMessage = stringResource(R.string.last_location_used)
    val getCurrentLocationContentDesc = stringResource(R.string.get_current_location)
    val getLastLocationContentDesc = stringResource(R.string.get_last_location)
    val foundItButtonContentDesc = stringResource(R.string.found_it_button)

    // Calculate distance to treasure
    val distanceToTreasure = remember(userLocation, currentClue) {
        currentClue?.let {
            LocationService.calculateDistanceInMeters(
                userLocation.latitude,
                userLocation.longitude,
                it.latitude,
                it.longitude
            )
        } ?: 0.0
    }

    // Determine hint message based on distance
    val distanceHintMessage = when {
        distanceToTreasure <= 10 -> stringResource(R.string.distance_very_close)
        distanceToTreasure <= 50 -> stringResource(R.string.distance_getting_close)
        else -> stringResource(R.string.distance_far_away)
    }

    // Distance text
    val distanceText = stringResource(R.string.distance_to_treasure, distanceToTreasure)

    // Show dialog for incorrect location
    if (showIncorrectLocationDialog) {
        AlertDialog(
            onDismissRequest = { showIncorrectLocationDialog = false },
            title = { Text("Not Quite There Yet!") },
            text = { Text("You haven't reached the correct location yet. Keep exploring based on the clue!") },
            confirmButton = {
                Button(
                    onClick = { showIncorrectLocationDialog = false }
                ) {
                    Text("Continue Searching")
                }
            },
            backgroundColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Clue card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Clue #${(currentClue?.id?.plus(1)) ?: 1}",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = currentClue?.text ?: stringResource(R.string.app_name),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { showHint = !showHint },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(if (showHint) stringResource(R.string.hide_hint) else stringResource(R.string.show_hint))
                    }

                    if (showHint) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentClue?.hint ?: "",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
            
            // Timer card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.time_elapsed, elapsedTime),
                        style = MaterialTheme.typography.h6
                    )
                }
            }
            
            // Location info card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.your_location),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    if (showLocationMessage) {
                        Text(
                            text = locationMessage,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    Text(
                        text = "Latitude: ${userLocation.latitude}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Longitude: ${userLocation.longitude}",
                        style = MaterialTheme.typography.body1
                    )

                    // Add distance hint
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Distance info card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = when {
                            distanceToTreasure <= 10 -> Color(0xFFE8F5E9) // Light green, very close
                            distanceToTreasure <= 50 -> Color(0xFFFFF9C4) // Light yellow, getting close
                            else -> Color(0xFFFFEBEE) // Light red, far away
                        },
                        elevation = 2.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = distanceText,
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = distanceHintMessage,
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Location buttons column - Changed from Row to Column
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Get current location button
                        Button(
                            onClick = {
                                fusedLocationClient.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    null
                                ).addOnSuccessListener { location ->
                                    location?.let {
                                        viewModel.updateUserLocation(it.latitude, it.longitude)
                                        locationMessage = locationUpdatedMessage
                                        showLocationMessage = true
                                    } ?: run {
                                        locationMessage = locationErrorMessage
                                        showLocationMessage = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = getCurrentLocationContentDesc,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(stringResource(R.string.get_current_location))
                        }
                        
                        // Add space between buttons
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Get last location button
                        Button(
                            onClick = {
                                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                    location?.let {
                                        viewModel.updateUserLocation(it.latitude, it.longitude)
                                        locationMessage = lastLocationUsedMessage
                                        showLocationMessage = true
                                    } ?: run {
                                        locationMessage = locationErrorMessage
                                        showLocationMessage = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Place,
                                contentDescription = getLastLocationContentDesc,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(stringResource(R.string.get_last_location))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Quit button
                OutlinedButton(
                    onClick = { onQuit() },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = MaterialTheme.colors.error
                    ),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(stringResource(R.string.quit_button))
                }
                
                // Found It button
                Button(
                    onClick = {
                        val isCorrect = viewModel.checkLocation()
                        if (isCorrect) {
                            onFoundIt(true)
                        } else {
                            // Show incorrect location dialog instead of navigating
                            showIncorrectLocationDialog = true
                            // Call onFoundIt with false to ensure the timer keeps running
                            onFoundIt(false)
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    ),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = foundItButtonContentDesc,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(stringResource(R.string.found_it_button))
                }
            }
        }
    }
}
