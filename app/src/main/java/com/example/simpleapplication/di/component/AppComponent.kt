package com.example.simpleapplication.di.component

import com.example.simpleapplication.MainApplication
import com.example.simpleapplication.di.module.AppModule
import com.example.simpleapplication.di.module.BuildersModule
import com.example.simpleapplication.di.module.NetModule
import com.example.simpleapplication.di.module.WorkerModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AndroidInjectionModule::class,
        BuildersModule::class,
        AppModule::class,
        NetModule::class,
        WorkerModule::class)
)
interface AppComponent {
    fun inject(app: MainApplication)
}