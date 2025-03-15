# Treasure Hunt App

A location-based treasure hunt mobile application built with Kotlin and Jetpack Compose for Android. The app creates an interactive experience where users follow clues to find real-world locations.

## Overview

Treasure Hunt is an engaging mobile app that guides users through a series of location-based challenges. Users need to physically travel to different locations based on textual clues. The app verifies the user's location using GPS and provides feedback when they've found the correct location.

## Features

- **Location-based gameplay**: Uses device GPS to verify when users have found treasure locations
- **Progressive clue system**: Presents a series of clues that lead to different locations
- **Distance indicator**: Shows how far users are from the target location
- **Timer**: Tracks how long it takes to complete the treasure hunt
- **Hint system**: Provides additional guidance when needed
- **Completion screen**: Displays final time and summary when the hunt is completed

## Project Structure

The app follows MVVM (Model-View-ViewModel) architecture with clean separation of concerns:

### Key Components

- **Model**
  - `Clue.kt`: Defines the data structure for treasure hunt clues
  - `TreasureRepository.kt`: Provides access to clue data
  
- **ViewModel**
  - `TreasureHuntViewModel.kt`: Manages app state and business logic
  
- **View (UI)**
  - `MainActivity.kt`: Main entry point of the application
  - `NavGraph.kt`: Defines the navigation flow between screens
  - Screens:
    - `StartScreen.kt`: Welcome screen with start button
    - `PermissionsScreen.kt`: Handles location permission requests
    - `ClueScreen.kt`: Displays clues and handles location verification
    - `ClueSolvedScreen.kt`: Shows information when a clue is solved
    - `CompletionScreen.kt`: Final screen displayed upon completion

- **Services**
  - `LocationService.kt`: Handles location-related functionality

## Data Management

The app uses a streamlined approach to data management:

- Clue data is directly defined in the `TreasureRepository` class
- Each clue includes text description, hint, coordinates, and information about the location
- The repository provides easy access to all clues as well as convenient constants for important coordinates
- This approach provides better type safety and makes it easy to modify or extend the treasure hunt

## Location-based Features

The app demonstrates several location-based capabilities:

- Runtime permission handling for location access
- Foreground location access
- Distance calculation between user and target locations
- Precise location verification (within ~30 meters)

## Permissions

The app requires the following permissions:
- `ACCESS_FINE_LOCATION`: For precise location tracking
- `ACCESS_COARSE_LOCATION`: As a fallback for approximate location

These permissions are requested at runtime with clear explanations to the user.

## Technologies Used

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit for building native UI
- **Kotlin Coroutines & Flow**: For asynchronous programming
- **Android ViewModel**: For state management
- **Android Navigation Component**: For screen navigation
- **Google Play Services Location**: For location services

## How to Run

1. Clone the repository
2. Open the project in Android Studio
3. Connect an Android device or use an emulator
4. Click Run

For the best experience, use the app on a real device with GPS capabilities when testing location features.

## Customizing Locations

The app currently features the following locations:
1. Test location (always detected at current position)
2. Los Angeles International Airport (LAX)
3. Portland International Airport (PDX)
4. Oregon State University (OSU)

To customize the treasure hunt with different locations:

1. Open `app/src/main/java/com/example/treasurehunt/data/TreasureRepository.kt`
2. Modify the clue data in the `loadClues()` function with your desired locations
3. Update the coordinates in the `ClueData` object if needed

## Author

Zhenghui Yin  
Oregon State University  
CS 492 
