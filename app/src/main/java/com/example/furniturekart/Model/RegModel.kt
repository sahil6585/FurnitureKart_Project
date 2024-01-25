package com.example.furniturekart.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegModel
{
    @Expose
    @SerializedName("phone")
    var phone=""

    @Expose
    @SerializedName("password")
    var password=""
}