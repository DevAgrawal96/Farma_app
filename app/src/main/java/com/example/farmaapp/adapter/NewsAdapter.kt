package com.example.farmaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.farmaapp.R
import com.example.farmaapp.databinding.NewsArticleItemBinding
import com.example.farmaapp.model.newsModels.Articles
import com.example.farmaapp.model.newsModels.Result
import com.example.farmaapp.utils.ChangeFragment
import com.example.farmaapp.viewholder.NewsViewHolder

class NewsAdapter(private val callback: ChangeFragment, private val flag: Boolean) :
    Adapter<NewsViewHolder>() {
    private var data = ArrayList<Result>()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        context = parent.context
        val binding =
            NewsArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (flag) {
            return data.size
        }
        return data.size/3
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.newsTitle.text = data[position].title
        holder.binding.newsSummary.text = data[position].body
        Glide.with(context).load(data[position].image).placeholder(R.drawable.news_placeholder)
            .into(holder.binding.imageView)
        holder.binding.dateTimePub.text = context.getString(
            R.string.publish_s,
            data[position].dateTimePub.split("T")[0].replace("-", "/")
        )

        holder.binding.newsContainer.setOnClickListener {
            callback.next(data, position)
        }
    }

    fun setData(newData: ArrayList<Result>) {
        val newsDiffUtil = NewsDiffUtil(data, newData)
        val newsDiff = DiffUtil.calculateDiff(newsDiffUtil)
        data = newData
        newsDiff.dispatchUpdatesTo(this)

    }

    class NewsDiffUtil(
        private val oldData: ArrayList<Result>,
        private val newData: ArrayList<Result>
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
            return oldData[oldItemPosition].title == newData[newItemPosition].title
        }

    }
}