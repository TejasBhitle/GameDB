package com.codeblooded.gamedb.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by tejas on 10/22/17.
 */
class Genre() : Parcelable {

    var id: String = ""
    var name: String = ""
    var url: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Genre> {
        override fun createFromParcel(parcel: Parcel): Genre = Genre(parcel)

        override fun newArray(size: Int): Array<Genre?> = arrayOfNulls(size)
    }

}