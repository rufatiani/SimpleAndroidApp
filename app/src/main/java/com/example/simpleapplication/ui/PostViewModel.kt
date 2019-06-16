package com.example.simpleapplication.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpleapplication.model.repository.PostRepository
import com.example.simpleapplication.model.Post
import com.example.simpleapplication.model.service.SendWorker
import com.example.simpleapplication.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import java.util.concurrent.TimeUnit.MILLISECONDS

class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val utils: Utils
) : ViewModel() {

    var postsResult: MutableLiveData<List<Post>> = MutableLiveData()
    var postsError: MutableLiveData<String> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()

    fun postsResult(): LiveData<List<Post>> {
        return postsResult
    }

    fun postsError(): LiveData<String> {
        return postsError
    }

    fun loadPosts() {
        val disposableObserver = object : DisposableObserver<List<Post>>() {
            override fun onComplete() {

            }

            override fun onNext(posts: List<Post>) {
                postsResult.postValue(posts)
            }

            override fun onError(e: Throwable) {
                postsError.postValue(e.message)
            }
        }

        postRepository.getPosts()?.subscribeOn(Schedulers.newThread())?.observeOn(AndroidSchedulers.mainThread())
            ?.debounce(400, MILLISECONDS)?.subscribe(disposableObserver)
    }

    private fun sendPost(post: Post) {
       val disposableObserver = object : DisposableObserver<Post>() {
                override fun onComplete() {
                }

                override fun onNext(post: Post) {
                    val id = post
                    Log.e("ITEMS SUCCESS ", id.toString())
                }

                override fun onError(e: Throwable) {
                    Log.e("ERROR *** ", e.message)
                }

            }

        postRepository.sendPost(post)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, MILLISECONDS)
            .subscribe(disposableObserver)

        compositeDisposable.addAll(disposableObserver)

    }

    fun savePost(post: Post) {
        postRepository.savePost(post)

        val hasConnection = utils.isConnectedToInternet();
        if(hasConnection) {
            post.title?.let { sendPost(post) }
        }
    }

    fun postsReset(): LiveData<List<Post>> {
        return MutableLiveData()
    }

    fun disposeElements(){
        if(null != compositeDisposable && !compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

}