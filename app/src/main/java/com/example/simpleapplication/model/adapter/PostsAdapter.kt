package com.example.simpleapplication.model.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.simpleapplication.R
import com.example.simpleapplication.model.Post

class PostsAdapter(
    posts: List<Post>?) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
    private var postList = ArrayList<Post>()

    init {
        this.postList = posts as ArrayList<Post>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(
            R.layout.item_post,
            parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postItem = postList[position]
        holder?.postListItem(postItem)
    }

    fun addPost(posts: List<Post>){
        val initPosition = postList.size
        postList.addAll(posts)
        notifyItemRangeInserted(initPosition, postList.size)
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var postTittle = itemView.findViewById<TextView>(R.id.tvItemTittle)
        var postStatus = itemView.findViewById<TextView>(R.id.tvItemStatus)

        fun postListItem(postItem: Post) {
            postTittle.text = postItem.title
            if(postItem.isPending != null) {
                if (postItem.isPending) {
                    postStatus.visibility = View.VISIBLE
                } else {
                    postStatus.visibility = View.INVISIBLE
                }
            }else{
                postStatus.visibility = View.INVISIBLE
            }
        }
    }
}