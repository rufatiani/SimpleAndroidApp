package com.example.simpleapplication.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.simpleapplication.model.adapter.PostsAdapter
import com.example.simpleapplication.R
import com.example.simpleapplication.model.api.ApiClient
import com.example.simpleapplication.model.api.ApiInterface
import com.example.simpleapplication.model.Post
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var postViewModelFactory: PostViewModelFactory
    lateinit var postViewModel: PostViewModel
    var postsAdapter = PostsAdapter(ArrayList())

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeRecycler()

        AndroidInjection.inject(this)

        postViewModel = ViewModelProviders.of(this, postViewModelFactory).get(
            PostViewModel::class.java)

        postViewModel.loadPosts()

        postViewModel.postsResult().observe(this,
            Observer<List<Post>> {
                Log.e("RESULT", "Hello $it")
                if (it != null) {
                    postsAdapter.addPost(it)
                    rvPosts.adapter = postsAdapter
                }
            })

        postViewModel.postsError().observe(this, Observer<String>{
            Log.e("ERROR", "Hello error $it")
            //hello_world_textview.text = "Hello error $it"
        })

        showPosts()
        //sendProcess()

    }

    private fun initializeRecycler() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvPosts.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
        }
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
                    Log.e("ITEMS **** ", posts.toString())
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
        return apiInterface.sendPost(post)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        postViewModel.disposeElements()
        super.onDestroy()
    }
}