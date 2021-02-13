package com.shokker.formsignaler

import com.shokker.formsignaler.model.MainContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class FunctionPresenterModule {
    @Provides
    fun provideSignalFunctionPresenter(): MainContract.SignalFunctionPresenter
    {
        return SignalFunctionPresenterImpl()
    }
}

class SignalFunctionPresenterImpl: MainContract.SignalFunctionPresenter
{
    override var currentSignalFunction: MainContract.SignalFunction? = null

    override fun loadActiveFunction(signalFunction: MainContract.SignalFunction) {
        currentSignalFunction = signalFunction
    }

    override fun saveActiveFunction() {
        TODO("Not yet implemented")
    }

}