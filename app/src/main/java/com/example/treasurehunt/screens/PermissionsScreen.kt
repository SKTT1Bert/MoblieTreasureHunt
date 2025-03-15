/*
 * Assignment 6
 * PermissionsScreen.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * OSU
 * CS 492
 */

package com.example.treasurehunt.screens

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.treasurehunt.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts


@Composable
fun PermissionsScreen(
    onPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Permission dialog state
    var showPermissionDialog by remember { mutableStateOf(false) }
    
    // State variable to track permission status
    var permissionState by remember { mutableStateOf(PermissionState.INITIAL) }
    
    // Permission request launcher
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            permissionState = if (isGranted) {
                PermissionState.GRANTED
            } else {
                PermissionState.DENIED
            }
        }
    )

    // Check initial permission state
    LaunchedEffect(Unit) {
        // First check permission status
        if (hasLocationPermissions(context)) {
            permissionState = PermissionState.GRANTED
        }
    }

    // Show permission request dialog
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Location Permission Request") },
            text = { 
                Text(
                    "This app needs access to your location to function properly.\n\n" +
                    "The Treasure Hunt game needs to know your location to verify when you've found the correct places. Without access to your location, you won't be able to play the game.\n\n" +
                    "Would you like to continue and grant permission?"
                ) 
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDialog = false
                        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text("Accept")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { 
                        showPermissionDialog = false 
                        // User explicitly denied the permission
                        permissionState = PermissionState.DENIED
                    }
                ) {
                    Text("Deny")
                }
            },
            backgroundColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.permissions_title)) },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display different UI based on permission state
            when (permissionState) {
                PermissionState.INITIAL -> {
                    // Initial request state
                    InitialPermissionContent(
                        onRequestPermission = {
                            // Show dialog instead of directly requesting permission
                            showPermissionDialog = true
                        }
                    )
                }
                PermissionState.DENIED -> {
                    // Permission denied state
                    DeniedPermissionContent(
                        onRequestPermission = {
                            // Show dialog instead of directly requesting permission
                            showPermissionDialog = true
                        }
                    )
                }
                PermissionState.GRANTED -> {
                    // Permission granted state - with user confirmation button
                    GrantedPermissionContent(onContinue = onPermissionsGranted)
                }
            }
        }
    }
}

// Initial permission request content
@Composable
private fun InitialPermissionContent(onRequestPermission: () -> Unit) {
    Icon(
        Icons.Default.LocationOn,
        contentDescription = "Location permission",
        modifier = Modifier
            .size(80.dp)
            .padding(bottom = 24.dp),
        tint = MaterialTheme.colors.primary
    )
    
    Text(
        text = stringResource(R.string.permissions_title),
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(R.string.permissions_explanation),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Deny button
        OutlinedButton(
            onClick = { /* User chooses not to request permission, stays in initial state */ },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(end = 8.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.error
            )
        ) {
            Text(
                text = "Later",
                style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold)
            )
        }
        
        // Request permission button
        Button(
            onClick = onRequestPermission,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(start = 8.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant
            )
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Grant Location Permissions",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = stringResource(R.string.grant_permissions),
                style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

// Permission denied content
@Composable
private fun DeniedPermissionContent(onRequestPermission: () -> Unit) {
    Icon(
        Icons.Default.Warning,
        contentDescription = "Permission denied",
        modifier = Modifier
            .size(80.dp)
            .padding(bottom = 24.dp),
        tint = MaterialTheme.colors.error
    )
    
    Text(
        text = "Location Permission Denied",
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color(0xFFFFF3F3) // Light red background for warning
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "This app cannot function without location permissions",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "The Treasure Hunt game needs to know your location to verify when you've found the correct places. Without access to your location, you won't be able to play the game.",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Exit button
        OutlinedButton(
            onClick = { /* User chooses to exit the app */ },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(end = 8.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.error
            )
        ) {
            Text(
                text = "Exit App",
                style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold)
            )
        }
        
        // Request permission again button
        Button(
            onClick = onRequestPermission,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(start = 8.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error
            )
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Try Again",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Grant Permission",
                style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    Text(
        text = "If you've permanently denied permissions, you'll need to enable them in your device settings",
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Center,
        color = Color.Gray
    )
}

// Permission granted content - Modified to add continue button
@Composable
private fun GrantedPermissionContent(onContinue: () -> Unit) {
    // Custom bright green location icon
    Icon(
        Icons.Default.LocationOn,
        contentDescription = "Location permission granted",
        modifier = Modifier
            .size(80.dp)
            .padding(bottom = 24.dp),
        tint = Color(0xFF00E676) // Bright green
    )
    
    Text(
        text = "Permission Granted!",
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    
    Text(
        text = "You're ready to start your treasure hunt.",
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 32.dp)
    )
    
    // Updated button to match screenshot - bright green with place icon
    Button(
        onClick = onContinue,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF00E676) // Bright green
        )
    ) {
        Icon(
            Icons.Default.LocationOn,
            contentDescription = "Continue to Treasure Hunt",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "Continue to Treasure Hunt",
            style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

// Permission state enum
private enum class PermissionState {
    INITIAL,   // Initial state, permission not requested
    GRANTED,   // Permission granted
    DENIED     // Permission denied
}

// Helper to check if we already have location permissions
fun hasLocationPermissions(context: android.content.Context): Boolean {
    val fineLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fineLocation == android.content.pm.PackageManager.PERMISSION_GRANTED &&
            coarseLocation == android.content.pm.PackageManager.PERMISSION_GRANTED
}

