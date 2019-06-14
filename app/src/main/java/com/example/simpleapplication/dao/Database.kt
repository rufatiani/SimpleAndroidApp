package com.example.simpleapplication.dao


import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.simpleapplication.model.Post

@Database(entities = arrayOf(Post::class), version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun postDao(): PostDao
}