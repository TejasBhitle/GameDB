package com.codeblooded.gamedb.model

import android.os.Parcel
import android.os.Parcelable
import com.codeblooded.gamedb.Constants
import org.json.JSONArray



/**
 * Created by tejas on 8/4/17.
 */
 class Game() :Parcelable {
    var id: Long = 0
    var name : String = ""
    var description : String = ""
    var url: String = ""
    var storyline: String =""
    var user_rating: String = ""
    var critic_rating: String = ""
    var img_url : String = ""
    var bg_url : String = ""
    var games: JSONArray = JSONArray()
    var release_date: String = ""
    var screenshots: JSONArray = JSONArray()
    var videos: JSONArray = JSONArray()


    fun getHashMap(): HashMap<String, Any> {
        val hashmap = HashMap<String, Any>()
        hashmap.put(Constants.ID,id)
        hashmap.put(Constants.NAME, name)
        hashmap.put(Constants.DESCRIPTION, description)
        hashmap.put(Constants.URL, url)
        hashmap.put(Constants.STORYLINE,storyline)
        hashmap.put(Constants.USER_RATING,user_rating)
        hashmap.put(Constants.CRITIC_RATING,critic_rating)
        hashmap.put(Constants.IMG_URL,img_url)
        hashmap.put(Constants.BG_URL,bg_url)
        hashmap.put(Constants.RELEASE_DATE,release_date)
        return hashmap
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        description = parcel.readString()
        url = parcel.readString()
        storyline = parcel.readString()
        user_rating = parcel.readString()
        critic_rating = parcel.readString()
        img_url = parcel.readString()
        bg_url = parcel.readString()
        games = JSONArray(parcel.readString())
        release_date = parcel.readString()
        screenshots = JSONArray(parcel.readString())
        videos = JSONArray(parcel.readString())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(storyline)
        parcel.writeString(user_rating)
        parcel.writeString(critic_rating)
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
