package com.shokker.formsignaler.DIModules

import android.content.Context

import com.shokker.formsignaler.SignalFunctionPresenterImpl
import com.shokker.formsignaler.model.MainContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
/*
@InstallIn(SingletonComponent::class)
@Module
class GeneratorPresenterModule {
    @Provides
    fun provideGeneratorPresenter(@ApplicationContext context: Context): MainContract.GeneratorPresenter
    {
        return GeneratorPresenterImpl( context)
    }
}*/