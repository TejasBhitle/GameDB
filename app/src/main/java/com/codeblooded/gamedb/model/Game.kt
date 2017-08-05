package com.codeblooded.gamedb.model

/**
 * Created by tejas on 8/4/17.
 */
data class Game(var id: Int,
                var name: String? = null,
                var summary : String? = null,
                var storyline: String? = null,
                var popularity: Double,
                var rating: Double
                )
