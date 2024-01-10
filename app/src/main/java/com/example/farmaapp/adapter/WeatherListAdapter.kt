package com.example.farmaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants.CHAR_PATTERN_IN
import com.example.farmaapp.custom.Constants.CHAR_PATTERN_OUT
import com.example.farmaapp.custom.Constants.T
import com.example.farmaapp.custom.Constants.UTC
import com.example.farmaapp.databinding.WeatherItemBinding
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.viewholder.WeatherViewHolder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WeatherListAdapter : Adapter<WeatherViewHolder>() {
    lateinit var context: Context
    private var data: Hourly = Hourly(
        ArrayList<Int>() as List<Int>,
        ArrayList<Int>() as List<Int>,
        ArrayList<Double>() as List<Double>,
        ArrayList<String>() as List<String>,
        ArrayList<Int>() as List<Int>
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        context = parent.context
        val binding =
            WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.time.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.binding.date.text = data.time[position].split("T")[0].replace("-", "/")
        //format string 24Hr to 12Hr
        val utc = TimeZone.getTimeZone(UTC)
        val inputFormat: DateFormat = SimpleDateFormat(CHAR_PATTERN_IN,
            Locale.ENGLISH)
        inputFormat.timeZone = utc
        val outputFormat: DateFormat = SimpleDateFormat(CHAR_PATTERN_OUT,
            Locale.ENGLISH)
        outputFormat.timeZone = utc
        val dateInput = inputFormat.parse(data.time[position].split(T)[1])
        val output = outputFormat.format(dateInput!!)

        holder.binding.time.text = output
        holder.binding.temp.text =
            context.getString(R.string.temp_, data.temperature_2m[position].toString())
        holder.binding.humidity.text = "${data.relativehumidity_2m[position]} %"
    }

    fun setData(newData: Hourly) {
        val weatherDiffUtil = WeatherDiffUtil(data, newData)
        val weatherDiff = DiffUtil.calculateDiff(weatherDiffUtil)
        data = newData
        weatherDiff.dispatchUpdatesTo(this)

    }

    class WeatherDiffUtil(
        private val oldData: Hourly,
        private val newData: Hourly
    ) :
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