package com.example.simpleapplication.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.example.simpleapplication.dao.Database
import com.example.simpleapplication.dao.PostDao
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
}