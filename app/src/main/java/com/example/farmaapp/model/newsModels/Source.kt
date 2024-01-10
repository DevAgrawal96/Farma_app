package com.example.farmaapp.model.newsModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val dataType: String,
    val title: String,
    val uri: String
) : Parcelable