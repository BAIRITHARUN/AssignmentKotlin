package com.infy.assignmentkotlin.main_activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class TitilesModel {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("rows")
    @Expose
    var rows: ArrayList<Row>? = null

}