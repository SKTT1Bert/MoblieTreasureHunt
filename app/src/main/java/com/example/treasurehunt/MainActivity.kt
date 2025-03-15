/*
 * Assignment 6
 * MainActivity.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * Oregon State University
 * CS 492
 */

package com.example.treasurehunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import com.example.treasurehunt.navigation.TreasureNavGraph
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.treasurehunt.ui.theme.TreasurehuntTheme

class MainActivity : ComponentActivity() {

    // FusedLocationProviderClient for location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    
    // ViewModel 
    private lateinit var viewModel: TreasureHuntViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        // ViewModel
        viewModel = ViewModelProvider(this)[TreasureHuntViewModel::class.java]

        setContent {
            TreasurehuntApp(viewModel, fusedLocationClient)
        }
    }
}

@Composable
fun TreasurehuntApp(
    viewModel: TreasureHuntViewModel,
    fusedLocationClient: FusedLocationProviderClient
) {
    TreasurehuntTheme {
    
        val navController = rememberNavController()
        
      
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            // fusedLocationClient
            TreasureNavGraph(
                navController = navController,
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                fusedLocationClient = fusedLocationClient
            )
        }
    }
}
