package com.example.simpleapplication.model.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.simpleapplication.model.Post
import io.reactivex.Single

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun queryPosts(): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post)

    @Query("UPDATE posts SET `is-pending`=0 WHERE id = :id")
    fun updatePost(id: Int)
}