package com.example.simpleapplication.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class PostViewModelFactory @Inject constructor(
    private val postViewModel: PostViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java!!)) {
            return postViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}