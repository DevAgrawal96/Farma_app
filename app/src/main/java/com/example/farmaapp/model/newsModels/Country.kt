package com.example.farmaapp.model.newsModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val label: Label,
    val type: String
) : Parcelable