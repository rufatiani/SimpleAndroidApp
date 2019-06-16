package com.example.simpleapplication.model.repository

import android.os.AsyncTask
import android.util.Log
import com.example.simpleapplication.utils.Utils
import com.example.simpleapplication.model.Post
import com.example.simpleapplication.model.api.ApiInterface
import com.example.simpleapplication.model.dao.PostDao
import io.reactivex.Observable
import javax.inject.Inject
import kotlinx.coroutines.*

class PostRepository @Inject constructor(val apiInterface: ApiInterface,
                                         val postDao: PostDao, val utils: Utils
) {

    fun getPosts(): Observable<List<Post>>?  {
        val hasConnection = utils.isConnectedToInternet()

        if (hasConnection){
            var observableFromApi: Observable<List<Post>>? = null
            var observableFromDb: Observable<List<Post>>? = null

            var deferredFromApi = GlobalScope.async {
                getPostFromApi()
            }

            val deferredFromDb = GlobalScope.async {
                getPostFromDb()
            }

            runBlocking {
                observableFromApi = deferredFromApi.await()
                observableFromDb = deferredFromDb.await()
            }
            return Observable.concatArrayEager(observableFromApi, observableFromDb)
        }

        return runBlocking { getPostFromDb() }
    }

    suspend fun getPostFromApi(): Observable<List<Post>> {
        return apiInterface.getPosts()
            .doOnNext {
                for (item in it) {
                    postDao.insertPost(item)
                }
            }
    }

    suspend fun getPostFromDb(): Observable<List<Post>> {
        return postDao.queryPosts()
            .toObservable()
            .doOnNext {}
    }

    fun savePost(post: Post){
        InsertPostAsyncTask(postDao).execute(post)
    }

    fun updatePendingPosts(){
        UpdatePostAsyncTask(postDao).execute()
    }

    fun sendPost(post: Post ): Observable<Post> {
        return post.title.let {
            apiInterface.sendPost(it)
                .doOnNext {
                    updatePendingPosts()
                }
        }
    }

    fun getPendingPost(): Observable<List<Post>>{
        return postDao.queryPendingPost()
            .toObservable()
            .doOnNext {}
    }

    private class InsertPostAsyncTask(postDao: PostDao) : AsyncTask<Post, Unit, Long>() {
        val postDao  = postDao
        override fun doInBackground(vararg p0: Post?) : Long? {
           return postDao.insertPost(p0[0]!!)
        }
    }

    private class UpdatePostAsyncTask(postDao: PostDao) : AsyncTask<Void, Unit, Unit>() {
        val postDao  = postDao
        override fun doInBackground(vararg params: Void?) {
             postDao.updatePost()
        }
    }
}