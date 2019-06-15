package com.example.simpleapplication.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.simpleapplication.model.adapter.PostsAdapter
import com.example.simpleapplication.R
import com.example.simpleapplication.model.Post
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
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
                if (it != null) {
                    postsAdapter.addPost(it)
                    rvPosts.adapter = postsAdapter
                }
            })

        postViewModel.postsError().observe(this, Observer<String>{
            Log.e("ERROR", "$it")
        })

        bSend.setOnClickListener {
            try {
                postViewModel.savePost(Post(0,etPost.text.toString(), true))
                etPost.text.clear()
                Toast.makeText(applicationContext,"Posting..",Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(applicationContext,"Post failed!",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initializeRecycler() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvPosts.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        postViewModel.disposeElements()
        super.onDestroy()
    }
}