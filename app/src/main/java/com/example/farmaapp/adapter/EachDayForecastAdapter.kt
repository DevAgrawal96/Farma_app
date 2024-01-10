package com.example.farmaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.databinding.EachDayForecastItemBinding
import com.example.farmaapp.databinding.NextForecastItemBinding
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.model.newsModels.EachDayHourly
import com.example.farmaapp.utils.getKeyValue
import com.example.farmaapp.utils.getSevenDayData
import com.example.farmaapp.utils.log
import com.example.farmaapp.viewholder.EachDayForecastViewHolder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class EachDayForecastAdapter : RecyclerView.Adapter<EachDayForecastViewHolder>() {
    private lateinit var context: Context
    private var data = ArrayList<EachDayHourly>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EachDayForecastViewHolder {
        context = parent.context
        val binding =
            EachDayForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EachDayForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: EachDayForecastViewHolder, position: Int) {
        val utc = TimeZone.getTimeZone(Constants.UTC)

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

        val dateInput =
            inputFormat.parse(data[position].time.split("T")[1])
        val output = outputFormat.format(dateInput!!)
        holder.binding.forecastTime.text = output
        holder.binding.nextForecastTemp.text =
            context.getString(R.string.temp_, data[position].temperature_2m.toString())
        holder.binding.nextForecastImg.setImageResource(getKeyValue()[data[position].weathercode]!!.weather_img)

    }

    fun setData(newData: ArrayList<EachDayHourly>) {
        val forecastDiffUtil = ForecastDiffUtil(data, newData)
        val forecastDiff = DiffUtil.calculateDiff(forecastDiffUtil)
        data = newData
        forecastDiff.dispatchUpdatesTo(this)
    }

    class ForecastDiffUtil(
        private val oldData: ArrayList<EachDayHourly>,
        private val newData: ArrayList<EachDayHourly>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldData.size
        }

        override fun getNewListSize(): Int {
            return newData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition].time == newData[newItemPosition].time
        }

    }
}