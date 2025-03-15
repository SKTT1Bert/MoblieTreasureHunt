/*
 * Assignment 6
 * TreasureRepository.kt
 * Zhenghui Yin / yinizh@oregonstate.edu
 * OSU
 * CS 492
 */

package com.example.treasurehunt.data

import com.example.treasurehunt.model.Clue

/**
 * Repository that provides clue data for the treasure hunt app.
 * All clue data is now directly defined in this class rather than in XML resources.
 */
class TreasureRepository {
    
    /**
     * Returns a list of all clues for the treasure hunt
     */
    fun loadClues(): List<Clue> {
        return listOf(
            // Clue 0 (Special Test Clue - Always Correct)
            Clue(
                id = 0,
                text = "Grab some things for dinner but keep an eye on the time - An open-air Market with a large clock.",
                hint = "This is a test clue that will always be detected at your current location.",
                latitude = 88.0,
                longitude = 88.0,
                info = "This is a demonstration location. The Pike Place Market was founded in 1907 and is one of the oldest continuously operated public farmers' markets in the United States. The Market is Seattle's most popular tourist destination and the center of a thriving neighborhood of farmers, craftspeople, small businesses and residents.",
                isFinal = false
            ),
            
            // Clue 1 - Los Angeles International Airport
            Clue(
                id = 1,
                text = "Where travelers take to the skies in the City of Angels - A bustling international hub with a futuristic flying saucer-like landmark.",
                hint = "This major international airport has an iconic Theme Building and serves as the main gateway to Southern California.",
                latitude = 33.942153,
                longitude = -118.403605,
                info = "Los Angeles International Airport (LAX) is the primary international airport serving Los Angeles and Southern California. It is the largest and busiest airport on the West Coast of the United States. The airport features the famous Theme Building, designed in the Googie architecture style resembling a flying saucer on legs, which has become a cultural landmark and symbol of Los Angeles.",
                isFinal = false
            ),
            
            // Clue 2 - Portland International Airport
            Clue(
                id = 2,
                text = "Where travelers take to the skies, with luggage in tow. A busy hub for journeys near and far - Look for planes landing and departing.",
                hint = "This airport serves the Portland metropolitan area and is known for its distinctive carpet pattern.",
                latitude = 45.585270,
                longitude = -122.591718,
                info = "Portland International Airport (PDX) is the largest airport in Oregon, serving millions of passengers annually. Known for its art installations, food options from local restaurants, and the famous PDX carpet pattern that has become a cultural icon. The airport has been consistently recognized as one of the best airports in the United States.",
                isFinal = false
            ),
            
            // Clue 3 (Final) - Oregon State University
            Clue(
                id = 3,
                text = "Seek the home of the Beavers, where orange and black reign supreme - A prestigious university with a rich history of research and innovation.",
                hint = "This university in Corvallis, Oregon has been educating students since 1868.",
                latitude = 44.561810,
                longitude = -123.282267,
                info = "Oregon State University is a public research university established in 1868. As Oregon's land-grant university, it has contributed significantly to agricultural research, forestry, and marine sciences. The campus spans 400 acres and serves over 30,000 students annually.",
                isFinal = true
            )
        )
    }

    companion object {
        /**
         * Provides convenient access to specific clue data without loading the entire list
         */
        object ClueData {
            // Clue 0 - Always correct test clue
            const val TEST_CLUE_LATITUDE = 88.0
            const val TEST_CLUE_LONGITUDE = 88.0
            
            // Clue 1 - LAX
            const val LAX_LATITUDE = 33.942153
            const val LAX_LONGITUDE = -118.403605
            
            // Clue 2 - PDX
            const val PDX_LATITUDE = 45.585270
            const val PDX_LONGITUDE = -122.591718
            
            // Clue 3 - OSU
            const val OSU_LATITUDE = 44.561810
            const val OSU_LONGITUDE = -123.282267
        }
    }
}
