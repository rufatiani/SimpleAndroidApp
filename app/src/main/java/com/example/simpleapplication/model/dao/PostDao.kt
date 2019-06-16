package com.example.simpleapplication.model.dao

import androidx.room.*
import com.example.simpleapplication.model.Post
import io.reactivex.Single

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun queryPosts(): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post): Long

    @Query("UPDATE posts SET `is-pending`=0 WHERE `is-pending`=1 OR `is-pending`=null")
    fun updatePost()

    @Query("SELECT * FROM posts WHERE `is-pending` = null OR `is-pending` = 1")
    fun queryPendingPost(): Single<List<Post>>
}