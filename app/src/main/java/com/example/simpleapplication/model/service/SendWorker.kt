package com.example.simpleapplication.model.service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.WorkerFactory
import androidx.work.ListenableWorker
import com.example.simpleapplication.model.Post
import com.example.simpleapplication.model.repository.PostRepository
import com.example.simpleapplication.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class SendWorker @Inject constructor(
    private val appContext: Context,
    private val params: WorkerParameters,
    private val postRepository: PostRepository,
    private val utils: Utils
) : Worker(appContext, params){

    override fun doWork(): Result {
        if (utils.isConnectedToInternet()) {
            syncPosts()
        }

        return Result.success()
    }

    fun syncPosts(){
        val disposableObserver = object : DisposableObserver<List<Post>>() {
            override fun onComplete() {

            }

            override fun onNext(posts: List<Post>) {
                for (item in posts){
                     sendPost(item)
                }
            }

            override fun onError(e: Throwable) {
                Log.e("ERROR",e.message)
            }
        }

        postRepository.getPendingPost()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    private fun sendPost(post: Post) {
        val disposableObserver = object : DisposableObserver<Post>() {
            override fun onComplete() {
            }

            override fun onNext(post: Post) {
            }

            override fun onError(e: Throwable) {
                Log.e("ERROR *** ", e.message)
            }
        }

        postRepository.sendPost(post)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    class Factory @Inject constructor(
        val postRepository: PostRepository,
        val utils: Utils
        ): ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): Worker {
            return SendWorker(appContext, params, postRepository, utils)
        }
    }

}
