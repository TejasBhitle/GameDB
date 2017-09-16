package com.codeblooded.gamedb.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray

/**
 * Created by tejas on 8/4/17.
 */
 class Game() :Parcelable {
    var id: Int? = null
    var name : String? = null
    var description : String? = null
    var url: String? = null
    var storyline: String? = null
    var user_rating: Double = 0.0
    var critic_rating: Double = 0.0
    var img_url : String? = null
    var bg_url : String? = null
    var games: JSONArray = JSONArray()
    var release_date: String? = null
    var screenshots: JSONArray = JSONArray()
    var videos: JSONArray = JSONArray()

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        description = parcel.readString()
        url = parcel.readString()
        storyline = parcel.readString()
        user_rating = parcel.readDouble()
        critic_rating = parcel.readDouble()
        img_url = parcel.readString()
        bg_url = parcel.readString()
        games = JSONArray(parcel.readString())
        release_date = parcel.readString()
        screenshots = JSONArray(parcel.readString())
        videos = JSONArray(parcel.readString())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id!!)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(storyline)
        parcel.writeDouble(user_rating)
        parcel.writeDouble(critic_rating)
        parcel.writeString(img_url)
        parcel.writeString(bg_url)
        parcel.writeString(games.toString())
        parcel.writeString(release_date)
        parcel.writeString(screenshots.toString())
        parcel.writeString(videos.toString())
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game = Game(parcel)

        override fun newArray(size: Int): Array<Game?> = arrayOfNulls(size)
    }
}
