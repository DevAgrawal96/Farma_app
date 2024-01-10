package com.example.farmaapp.model

import android.os.Parcel
import android.os.Parcelable

data class Hourly(
    val is_day: List<Int>,
    val relativehumidity_2m: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("is_day"),
        TODO("relativehumidity_2m"),
        TODO("temperature_2m"),
        parcel.createStringArrayList()!!,
        TODO("weathercode")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hourly> {
        override fun createFromParcel(parcel: Parcel): Hourly {
            return Hourly(parcel)
        }

        override fun newArray(size: Int): Array<Hourly?> {
            return arrayOfNulls(size)
        }
    }
}