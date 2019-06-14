package com.example.simpleapplication.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
    tableName = "posts"
)
data class Post(

    @Json(name = "id")
    @PrimaryKey
    val id: String,

    @Json(name = "title")
    val title: String?

) : Serializable