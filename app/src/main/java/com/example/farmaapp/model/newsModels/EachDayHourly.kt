package com.example.farmaapp.model.newsModels

import android.os.Parcel
import android.os.Parcelable

data class EachDayHourly(
    val is_day: Int,
    val relativehumidity_2m: Int,
    val temperature_2m: Double,
    val time: String,
    val weathercode: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(is_day)
        parcel.writeInt(relativehumidity_2m)
        parcel.writeDouble(temperature_2m)
        parcel.writeString(time)
        parcel.writeInt(weathercode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EachDayHourly> {
        override fun createFromParcel(parcel: Parcel): EachDayHourly {
            return EachDayHourly(parcel)
        }

        override fun newArray(size: Int): Array<EachDayHourly?> {
            return arrayOfNulls(size)
        }
    }
}
