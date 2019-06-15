package com.example.simpleapplication.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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
    val title: String?,

    @ColumnInfo(name = "is-pending")
    val isPending: Boolean?

) : Serializable