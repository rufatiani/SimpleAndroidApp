package com.example.simpleapplication.di.module

import androidx.work.ListenableWorker
import com.example.simpleapplication.model.service.ChildWorkerFactory
import com.example.simpleapplication.model.service.SendWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass


@Module
abstract class WorkerModule {

    @MapKey
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class WorkerKey(val value: KClass<out ListenableWorker>)

    @Binds
    @IntoMap
    @WorkerKey(SendWorker::class)
    internal abstract fun bindMyWorkerFactory(worker: SendWorker.Factory): ChildWorkerFactory
}