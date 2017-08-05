package com.codeblooded.gamedb.model

/**
 * Created by tejas on 8/4/17.
 */
public class Game{

    private var id:Int? = null
    private var name: String? = null
    private var url: String? = null
    private var summary: String? = null
    private var storyline: String? = null
    private var popularity: Double? = null
    private var rating: Double? = null

    constructor(id: Int?, name: String?, url: String?, summary: String?, storyline: String?, popularity: Double?, rating: Double?) {
        this.id = id
        this.name = name
        this.url = url
        this.summary = summary
        this.storyline = storyline
        this.popularity = popularity
        this.rating = rating
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun getSummary(): String? {
        return summary
    }

    fun setSummary(summary: String) {
        this.summary = summary
    }

    fun getStoryline(): String? {
        return storyline
    }

    fun setStoryline(storyline: String) {
        this.storyline = storyline
    }

    fun getPopularity(): Double? {
        return popularity
    }

    fun setPopluarity(popularity: Double?) {
        this.popularity = popularity
    }

    fun getRating(): Double? {
        return rating
    }

    fun setRating(rating: Double?) {
        this.rating = rating
    }
}