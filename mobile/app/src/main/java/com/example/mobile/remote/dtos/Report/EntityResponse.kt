package com.example.mobile.remote.dtos.Report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntityResponse(
    val id:String,
    val name:String,
) : Parcelable
