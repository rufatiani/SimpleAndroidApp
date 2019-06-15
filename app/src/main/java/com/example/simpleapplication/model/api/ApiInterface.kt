package com.example.simpleapplication.model.api

import io.reactivex.Observable
import com.example.simpleapplication.model.Post
import retrofit2.http.*


interface ApiInterface {

    @GET("posts/")
    fun getPosts(): Observable<List<Post>>

    @POST("posts/")
    @FormUrlEncoded
    fun sendPost(@Field("title") title: String): Observable<Post>
}