package com.example.farmaapp.utils

interface ChangeFragment {
    fun next(data: Any,position : Int)
}
interface ChangeFragmentWithData {
    fun next(data: Any,date : String)
}