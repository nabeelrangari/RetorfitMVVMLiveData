package com.nabeel.erostestapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Movies (
    @SerializedName("page")
    @Expose
    var page: Int? = null,
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null,
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null,
    @SerializedName("results")
    @Expose
    var results: List<Result>? =
        null


)