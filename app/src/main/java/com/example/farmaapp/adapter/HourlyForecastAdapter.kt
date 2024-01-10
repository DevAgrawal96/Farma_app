package com.example.farmaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.custom.Constants.DAY_24
import com.example.farmaapp.custom.Constants.UTC
import com.example.farmaapp.databinding.HourlyWeatherForecastItemBinding
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.model.WeatherCode_
import com.example.farmaapp.utils.getKeyValue
import com.example.farmaapp.utils.log
import com.example.farmaapp.viewholder.HourlyViewHolder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class HourlyForecastAdapter : Adapter<HourlyViewHolder>() {
    private lateinit var context: Context
    private var data: Hourly = Hourly(
        ArrayList<Int>() as List<Int>,
        ArrayList<Int>() as List<Int>,
        ArrayList<Double>() as List<Double>,
        ArrayList<String>() as List<String>,
        ArrayList<Int>() as List<Int>
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        context = parent.context
        val binding =
            HourlyWeatherForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HourlyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return DAY_24
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val utc = TimeZone.getTimeZone(UTC)

        val inputFormat: DateFormat = SimpleDateFormat(
            Constants.CHAR_PATTERN_IN,
            Locale.ENGLISH
        )
        inputFormat.timeZone = utc

        val outputFormat: DateFormat = SimpleDateFormat(
            Constants.CHAR_PATTERN_OUT,
            Locale.ENGLISH
        )
        outputFormat.timeZone = utc

        val dateInput = inputFormat.parse(data.time[position].split("T")[1])

        val output = outputFormat.format(dateInput!!)

        holder.binding.weatherForecastTime.text = output
        holder.binding.temp.text =
            context.getString(R.string.temp_, data.temperature_2m[position].toString())
        holder.binding.weatherConditionImg.setImageResource(getKeyValue()[data.weathercode[position]]!!.weather_img)
    }

    fun setData(newData: Hourly) {
        val hourlyDiffUtil = HourlyDiffUtil(data, newData)
        val hourlyDiff = DiffUtil.calculateDiff(hourlyDiffUtil)
        data = newData
        hourlyDiff.dispatchUpdatesTo(this)

    }

    class HourlyDiffUtil(private val oldData: Hourly, private val newData: Hourly) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldData.time.size
        }

        override fun getNewListSize(): Int {
            return newData.time.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData.time[oldItemPosition] == newData.time[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData.temperature_2m[oldItemPosition] == newData.temperature_2m[newItemPosition]
        }

    }
}