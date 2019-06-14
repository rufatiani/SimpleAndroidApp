package com.example.simpleapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.simpleapplication.api.ApiClient
import com.example.simpleapplication.api.ApiInterface
import com.example.simpleapplication.model.Post
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showPosts()
        sendProcess()
    }

    private fun showPosts() {
        val postsResponse = getPosts()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

        val disposableObserver =
            postsResponse.subscribeWith(object : DisposableObserver<List<Post>>() {
                override fun onComplete() {
                }

                override fun onNext(posts: List<Post>) {
                    val listSize = posts.size
                    Log.e("ITEMS **** ", listSize.toString())
                }

                override fun onError(e: Throwable) {
                    Log.e("ERROR *** ", e.message)
                }

            })

        compositeDisposable.addAll(disposableObserver)

    }

    private fun sendProcess() {
        val postsResponse = sendPost("Testsss")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

        val disposableObserver =
            postsResponse.subscribeWith(object : DisposableObserver<Post>() {
                override fun onComplete() {
                }

                override fun onNext(post: Post) {
                    val id = post
                    Log.e("ITEMS SUCCESS ", id.toString())
                }

                override fun onError(e: Throwable) {
                    Log.e("ERROR *** ", e.message)
                }

            })

        compositeDisposable.addAll(disposableObserver)

    }

    private fun getPosts(): Observable<List<Post>> {
        val retrofit = ApiClient.getClient()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        return apiInterface.getPosts()
    }

    private fun sendPost(post: String ): Observable<Post> {
        val retrofit = ApiClient.getClient()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        return apiInterface.sendPost("Test")
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}