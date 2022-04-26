package com.example.codingchallenge.model

import com.google.gson.annotations.SerializedName

data class ImageData(var items: List<Items>)
data class Items(
    @SerializedName("title") var title: String? = null,
    @SerializedName("link") var link: String? = null,
    @SerializedName("media") var media: Media? = Media(),
    @SerializedName("date_taken") var dateTaken: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("published") var published: String? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("author_id") var authorId: String? = null,
    @SerializedName("tags") var tags: String? = null

)

data class Media(
    @SerializedName("m") var m: String? = null
)