package com.example.farmaapp.utils

import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.model.WeatherCode_
import com.example.farmaapp.model.newsModels.EachDayHourly
import com.example.farmaapp.modules.DataStoreProvider
import kotlinx.coroutines.flow.first


fun Activity.makeToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun log(tag: String, msg: String) {
    Log.e(tag, msg)
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}


suspend fun saveOnBoardingData(
    context: Context,
    dataStoreProvider: DataStoreProvider,
    key: String,
    value: Boolean
) {
    val dataStore = dataStoreProvider.getDataStoreInstance(context)
    val dataStoreKey = booleanPreferencesKey(key)
    dataStore.edit { onBoarding ->
        onBoarding[dataStoreKey] = value
    }
}

suspend fun getOnBoardingData(
    context: Context,
    dataStoreProvider: DataStoreProvider,
    key: String
): Boolean? {
    val dataStore = dataStoreProvider.getDataStoreInstance(context)
    val dataStoreKey = booleanPreferencesKey(key)
    val preferences = dataStore.data.first()
    return preferences[dataStoreKey]
}

suspend fun saveLocationData(
    context: Context,
    dataStoreProvider: DataStoreProvider,
    key: String,
    value: String
) {
    val dataStore = dataStoreProvider.getDataStoreInstance(context)
    val dataStoreKey = stringPreferencesKey(key)
    dataStore.edit { onBoarding ->
        onBoarding[dataStoreKey] = value
    }
}

suspend fun getLocationData(
    context: Context,
    dataStoreProvider: DataStoreProvider,
    key: String
): String? {
    val dataStore = dataStoreProvider.getDataStoreInstance(context)
    val dataStoreKey = stringPreferencesKey(key)
    val preferences = dataStore.data.first()
    return preferences[dataStoreKey]
}


fun getKeyValue(): HashMap<Int, WeatherCode_> {
    val wmoCode = HashMap<Int, WeatherCode_>()
    wmoCode.apply {
        set(0, WeatherCode_(Constants.CLEAR_SKY, R.drawable.clear_sky))
        set(1, WeatherCode_(Constants.MAINLY_CLEAR, R.drawable.mainly_clear))
        set(2, WeatherCode_(Constants.PARTLY_CLOUDY, R.drawable.partly_cloudy))
        set(3, WeatherCode_(Constants.CLOUDY, R.drawable.overcast))
        set(45, WeatherCode_(Constants.FOG, R.drawable.fog))
        set(51, WeatherCode_(Constants.DRIZZLE_LIGHT, R.drawable.drizzle_light))
        set(53, WeatherCode_(Constants.DRIZZLE_MODERATE, R.drawable.drizzle_moderate))
        set(55, WeatherCode_(Constants.DRIZZLE_DENSE_INTENSITY, R.drawable.drizzle_dense_intensity))
        set(56, WeatherCode_(Constants.FREEZING_DRIZZLE_LIGHT, R.drawable.freezing_drizzle_light))
        set(
            57,
            WeatherCode_(
                Constants.FREEZING_DRIZZLE_DENSE_INTENSITY,
                R.drawable.freezing_drizzle_dense_intensity
            )
        )
        set(61, WeatherCode_(Constants.RAIN_SLIGHT, R.drawable.rain_slight))
        set(63, WeatherCode_(Constants.RAIN_MODERATE, R.drawable.rain_moderate))
        set(65, WeatherCode_(Constants.RAIN_HEAVY_INTENSITY, R.drawable.rain_heavy_intensity))
        set(66, WeatherCode_(Constants.FREEZING_RAIN_LIGHT, R.drawable.freezing_rain_light))
        set(
            67,
            WeatherCode_(
                Constants.FREEZING_RAIN_HEAVY_INTENSITY,
                R.drawable.freezing_rain_heavy_intensity
            )
        )
        set(71, WeatherCode_(Constants.SNOW_FALL_SLIGHT, R.drawable.snow_fall_slight))
        set(73, WeatherCode_(Constants.SNOW_FALL_MODERATE, R.drawable.snow_fall_moderate))
        set(
            75,
            WeatherCode_(Constants.SNOW_FALL_HEAVY_INTENSITY, R.drawable.snow_fall_heavy_intensity)
        )
        set(77, WeatherCode_(Constants.SNOW_GRAINS, R.drawable.snow_grains))
        set(80, WeatherCode_(Constants.RAIN_SHOWERS_SLIGHT, R.drawable.rain_showers_slight))
        set(81, WeatherCode_(Constants.RAIN_SHOWERS_MODERATE, R.drawable.rain_showers_moderate))
        set(82, WeatherCode_(Constants.RAIN_SHOWERS_VIOLENT, R.drawable.rain_showers_violent))
        set(85, WeatherCode_(Constants.SNOW_SHOWERS_SLIGHT, R.drawable.snow_showers_slight))
        set(86, WeatherCode_(Constants.SNOW_SHOWERS_HEAVY, R.drawable.snow_showers_heavy))
        set(
            95,
            WeatherCode_(
                Constants.THUNDERSTORM_SLIGHT_OR_MODERATE,
                R.drawable.thunderstorm_slight_or_moderate
            )
        )
        set(
            96,
            WeatherCode_(Constants.THUNDERSTORM_WITH_SLIGHT, R.drawable.thunderstorm_with_slight)
        )
        set(
            99,
            WeatherCode_(
                Constants.THUNDERSTORM_WITH_HEAVY_HAIL,
                R.drawable.thunderstorm_with_heavy_hail
            )
        )
    }
    return wmoCode
}

fun getSevenDayData(hourly: Hourly): ArrayList<EachDayHourly> {
    val eachDayHourlyArray = ArrayList<EachDayHourly>()
    for (i in 11..hourly.time.size step 23) {
        log("index_",i.toString())
        eachDayHourlyArray.add(
            EachDayHourly(
                hourly.is_day[i],
                hourly.relativehumidity_2m[i],
                hourly.temperature_2m[i],
                hourly.time[i],
                hourly.weathercode[i]
            )
        )
    }
    return eachDayHourlyArray
}