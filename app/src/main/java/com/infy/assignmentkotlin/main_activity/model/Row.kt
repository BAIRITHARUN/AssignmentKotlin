package com.infy.assignmentkotlin.main_activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Row {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("imageHref")
    @Expose
    var imageHref: String? = null

}