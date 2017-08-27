package com.codeblooded.gamedb.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by tejas on 8/4/17.
 */
 class Game() :Parcelable {

    //var id: Int,
    var name : String? = null
    var description : String? = null
    var img_url : String? = null
    var bg_url : String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        description = parcel.readString()
        img_url = parcel.readString()
        bg_url = parcel.readString()
    }
    //var summary: String? = null
    //var storyline: String? = null
    //var popularity: Double = 0.0
    //var rating: Double = 0.0
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(img_url)
        parcel.writeString(bg_url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game = Game(parcel)

        override fun newArray(size: Int): Array<Game?> = arrayOfNulls(size)
    }
}
