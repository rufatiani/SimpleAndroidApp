package com.example.simpleapplication.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.simpleapplication.model.adapter.PostsAdapter
import com.example.simpleapplication.R
import com.example.simpleapplication.model.Post
import com.example.simpleapplication.model.service.SendWorker
import com.example.simpleapplication.model.service.SendWorkerFactory
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var postViewModelFactory: PostViewModelFactory

    lateinit var postViewModel: PostViewModel

    var postsAdapter = PostsAdapter(ArrayList())

    val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var factory: SendWorkerFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(factory).build())
        WorkManager.getInstance().enqueue( PeriodicWorkRequest
            .Builder(SendWorker::class.java, 1, TimeUnit.MILLISECONDS)
            .build()
        )

        postViewModel = ViewModelProviders.of(this, postViewModelFactory).get(
            PostViewModel::class.java)

        initializeRecycler()

        postViewModel.loadPosts()

        postViewModel.postsResult().observe(this,
            Observer<List<Post>> {
                if (it != null) {
                    postsAdapter.addPost(it)
                    rvPosts.adapter = postsAdapter
                }
            })

        bSend.setOnClickListener {
            try {
                postViewModel.savePost(Post(0,etPost.text.toString(), true))
                etPost.text.clear()
                postsAdapter.notifyDataSetChanged()
                //initializeRecycler()
                Toast.makeText(applicationContext,"Posting..",Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(applicationContext,"Post failed!",Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun initializeRecycler() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        gridLayoutManager.orientation = RecyclerView.VERTICAL
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