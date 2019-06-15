package com.example.simpleapplication.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.simpleapplication.model.repository.PostRepository
import com.example.simpleapplication.model.Post
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import java.util.concurrent.TimeUnit.MILLISECONDS

class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    var postsResult: MutableLiveData<List<Post>> = MutableLiveData()
    var postsError: MutableLiveData<String> = MutableLiveData()
    lateinit var disposableObserver: DisposableObserver<List<Post>>

    fun postsResult(): LiveData<List<Post>> {
        return postsResult
    }

    fun postsError(): LiveData<String> {
        return postsError
    }

    fun loadPosts() {
        disposableObserver = object : DisposableObserver<List<Post>>() {
            override fun onComplete() {

            }

            override fun onNext(posts: List<Post>) {
                postsResult.postValue(posts)
            }

            override fun onError(e: Throwable) {
                postsError.postValue(e.message)
            }
        }

        postRepository.getPosts()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun disposeElements(){
        if(null != disposableObserver && !disposableObserver.isDisposed) disposableObserver.dispose()
    }

}