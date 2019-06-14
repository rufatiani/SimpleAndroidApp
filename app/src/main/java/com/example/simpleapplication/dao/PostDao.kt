package com.example.simpleapplication.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.simpleapplication.model.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun queryCryptocurrencies(): LiveData<List<Post>>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    fun insertPost(post: Post)
}