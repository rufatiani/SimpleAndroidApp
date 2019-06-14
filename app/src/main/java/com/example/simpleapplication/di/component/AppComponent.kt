package com.example.simpleapplication.di.component

import android.app.Application
import com.example.simpleapplication.di.module.AppModule
import com.example.simpleapplication.di.module.BuildersModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(AndroidInjectionModule::class, BuildersModule::class, AppModule::class)
)
interface AppComponent {
    fun inject(app: Application)
}