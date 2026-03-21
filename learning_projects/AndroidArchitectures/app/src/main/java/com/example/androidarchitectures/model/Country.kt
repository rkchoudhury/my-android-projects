package com.example.androidarchitectures.model

import com.google.gson.annotations.SerializedName

/**
 * {
 *     "name": {
 *       "common": "British Virgin Islands",
 *       "official": "Virgin Islands",
 *       "nativeName": {
 *         "eng": {
 *           "official": "Virgin Islands",
 *           "common": "British Virgin Islands"
 *         }
 *       }
 *     }
 *   },
 */
data class Country(
    @SerializedName("name")
    val name: CountryDetails
) {
    val countryName: String get() = name.common ?: "Unknown"
}

data class CountryDetails(
    @SerializedName("common")
    val common: String?,

    @SerializedName("official")
    val official: String?,
)