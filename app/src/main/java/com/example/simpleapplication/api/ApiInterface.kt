package com.example.simpleapplication.api

import io.reactivex.Observable
import com.example.simpleapplication.model.Post
import retrofit2.http.*


interface ApiInterface {

    @GET("posts/")
    fun getPosts(): Observable<List<Post>>

    @POST("posts/")
    @FormUrlEncoded
    fun sendPost(@Field("tittle") tittle: String): Observable<Post>
}