package com.example.farmaapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity

data class WeatherModel(
    val current_weather: CurrentWeather,
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: Hourly,
    val hourly_units: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("current_weather"),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readParcelable(Hourly::class.java.classLoader)!!,
        TODO("hourly_units"),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(elevation)
        parcel.writeDouble(generationtime_ms)
        parcel.writeParcelable(hourly, flags)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(timezone)
        parcel.writeString(timezone_abbreviation)
        parcel.writeInt(utc_offset_seconds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherModel> {
        override fun createFromParcel(parcel: Parcel): WeatherModel {
            return WeatherModel(parcel)
        }

        override fun newArray(size: Int): Array<WeatherModel?> {
            return arrayOfNulls(size)
        }
    }
}