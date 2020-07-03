package com.payday.paydaybank.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@JsonClass(generateAdapter = true)
@Parcelize
data class Account(val id: Int,
                   @Json(name = "First Name")
                   val firstName: String,
                   @Json(name = "Last Name")
                   val lastName: String,
                   val gender: String,
                   val email: String,
                   val password: String,
                   val dob: String,
                   val phone: String
) : Parcelable