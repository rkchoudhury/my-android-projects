package com.example.navigationapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val age: Number,
    val dob: String,
): Parcelable