package com.example.simpleapplication.di.module

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.work.WorkerFactory
import com.example.simpleapplication.utils.Utils
import com.example.simpleapplication.ui.PostViewModelFactory
import com.example.simpleapplication.model.dao.Database
import com.example.simpleapplication.model.dao.PostDao
import com.example.simpleapplication.model.service.ChildWorkerFactory
import com.example.simpleapplication.model.service.SendWorker
import com.example.simpleapplication.model.service.SendWorkerFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application){

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun providePostDatabase(app: Application): Database = Room.databaseBuilder(app,
        Database::class.java, "simple_db").build()

    @Provides
    @Singleton
    fun providePostDao(database: Database): PostDao = database.postDao()

    @Provides
    fun providePostViewModelFactory(
        factory: PostViewModelFactory
    ): ViewModelProvider.Factory = factory

    @Provides
    @Singleton
    fun provideUtils(): Utils = Utils(app)

}