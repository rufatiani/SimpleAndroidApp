package com.example.simpleapplication.model.repository

import android.os.AsyncTask
import android.util.Log
import com.example.simpleapplication.utils.Utils
import com.example.simpleapplication.model.Post
import com.example.simpleapplication.model.api.ApiInterface
import com.example.simpleapplication.model.dao.PostDao
import io.reactivex.Observable
import javax.inject.Inject

class PostRepository @Inject constructor(val apiInterface: ApiInterface,
                                         val postDao: PostDao, val utils: Utils
) {

    fun getPosts(): Observable<List<Post>> {
        val hasConnection = utils.isConnectedToInternet()
        var observableFromApi: Observable<List<Post>>? = null
        if (hasConnection){
            observableFromApi = getPostFromApi()
        }
        val observableFromDb = getPostFromDb()

        return if (hasConnection) Observable.concatArrayEager(observableFromApi, observableFromDb)
        else observableFromDb
    }

    fun getPostFromApi(): Observable<List<Post>> {
        return apiInterface.getPosts()
            .doOnNext {
                Log.e("REPOSITORY API * ", it.toString())
                for (item in it) {
                    postDao.insertPost(item)
                }
            }
    }

    fun getPostFromDb(): Observable<List<Post>> {
        return postDao.queryPosts()
            .toObservable()
            .doOnNext {
                //Print log it.size :)
                Log.e("REPOSITORY DB *** ", it.toString())
            }
    }

    fun savePost(post: Post){
        InsertPostAsyncTask(postDao).execute(post)
        val hasConnection = utils.isConnectedToInternet()
        if(hasConnection){
            UpdatePostAsyncTask(postDao).execute(post.id)
        }
    }

    fun sendPost(post: String ): Observable<Post> {
        return apiInterface.sendPost(post)
            .doOnNext {
                UpdatePostAsyncTask(postDao).execute(it.id)
            }
    }

    private class InsertPostAsyncTask(postDao: PostDao) : AsyncTask<Post, Unit, Unit>() {
        val postDao  = postDao
        override fun doInBackground(vararg p0: Post?) {
            postDao.insertPost(p0[0]!!)
        }
    }

    private class UpdatePostAsyncTask(postDao: PostDao) : AsyncTask<Int, Unit, Unit>() {
        val postDao  = postDao
        override fun doInBackground(vararg p0: Int?) {
            postDao.updatePost(p0[0]!!)
        }
    }
}