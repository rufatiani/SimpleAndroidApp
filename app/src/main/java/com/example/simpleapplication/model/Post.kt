package com.example.simpleapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
    tableName = "posts"
)
data class Post(

    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @Json(name = "title")
    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "is-pending")
    val isPending: Boolean?

) : Serializable