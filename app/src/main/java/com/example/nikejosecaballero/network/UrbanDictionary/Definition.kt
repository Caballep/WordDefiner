package com.example.nikejosecaballero.network.UrbanDictionary

import com.google.gson.annotations.SerializedName

data class Definition(
    val definition: String,
    @SerializedName("thumbs_up")
    var thumbsUp: Int,
    @SerializedName("thumbs_down")
    var thumbsDown: Int,
    // Extra:
    var author: String
)