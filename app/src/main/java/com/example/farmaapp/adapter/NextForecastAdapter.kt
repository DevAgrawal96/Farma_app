package com.example.farmaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.farmaapp.R
import com.example.farmaapp.custom.Constants
import com.example.farmaapp.databinding.NextForecastItemBinding
import com.example.farmaapp.model.Hourly
import com.example.farmaapp.model.newsModels.EachDayHourly
import com.example.farmaapp.utils.ChangeFragment
import com.example.farmaapp.utils.ChangeFragmentWithData
import com.example.farmaapp.utils.getKeyValue
import com.example.farmaapp.utils.getSevenDayData
import com.example.farmaapp.utils.log
import com.example.farmaapp.viewholder.NextForecastViewHolder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NextForecastAdapter(private val callback: ChangeFragmentWithData) :
    Adapter<NextForecastViewHolder>() {
    private lateinit var context: Context
    private var data = Hourly(
        ArrayList<Int>() as List<Int>,
        ArrayList<Int>() as List<Int>,
        ArrayList<Double>() as List<Double>,
        ArrayList<String>() as List<String>,
        ArrayList<Int>() as List<Int>
    )
    private lateinit var eachDayHourly: ArrayList<EachDayHourly>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextForecastViewHolder {
        context = parent.context
        eachDayHourly = getSevenDayData(data)
        val binding =
            NextForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NextForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.time.size / 24
    }

    override fun onBindViewHolder(holder: NextForecastViewHolder, position: Int) {
        val utc = TimeZone.getTimeZone(Constants.UTC)

        val inputFormat: DateFormat = SimpleDateFormat(
            Constants.DATE_PATTER_IN,
            Locale.ENGLISH
        )
        inputFormat.timeZone = utc

        val outputFormat: DateFormat = SimpleDateFormat(
            Constants.DATE_PATTER_OUT,
            Locale.ENGLISH
        )
        outputFormat.timeZone = utc

        val dateInput =
            inputFormat.parse(eachDayHourly[position].time.split("T")[0].replace("-", "/"))
        val output = outputFormat.format(dateInput!!)

        holder.binding.day.text = output.split(",")[1]
        holder.binding.nextForecastDate.text = output.split(",")[0]
        holder.binding.nextForecastTemp.text =
            context.getString(R.string.temp_, eachDayHourly[position].temperature_2m.toString())
        holder.binding.nextForecastImg.setImageResource(getKeyValue()[eachDayHourly[position].weathercode]!!.weather_img)
        holder.binding.forecastContainer.setOnClickListener {
            callback.next(data, output)
        }
    }

    fun setData(newData: Hourly) {
        val forecastDiffUtil = ForecastDiffUtil(data, newData)
        val forecastDiff = DiffUtil.calculateDiff(forecastDiffUtil)
        data = newData
        forecastDiff.dispatchUpdatesTo(this)

    }

    class ForecastDiffUtil(
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