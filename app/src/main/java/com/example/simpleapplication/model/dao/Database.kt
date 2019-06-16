package com.example.simpleapplication.model.dao


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simpleapplication.model.Post

@Database(entities = arrayOf(Post::class), version = 6, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun postDao(): PostDao
}