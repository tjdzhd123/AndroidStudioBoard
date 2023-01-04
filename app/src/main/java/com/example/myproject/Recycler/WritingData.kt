package com.example.myproject.Recycler

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WritingData(val seq: Int?, val nickname: String?, val title: String?, val content: String?, val date: String?) :Parcelable{
}