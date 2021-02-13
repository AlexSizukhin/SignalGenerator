package com.shokker.formsignaler.DIModules

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.shokker.formsignaler.GenerationSettingsImpl
import com.shokker.formsignaler.data.SettingsDataDao
import com.shokker.formsignaler.data.SignalGeneratorDB
import com.shokker.formsignaler.model.MainContract
import com.shokker.formsignaler.model.RealSignalGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RealGeneratorModule {
    @Provides
    @Singleton
    fun provideRealGenerator(@ApplicationContext context: Context, settings:MainContract.GenerationSetting):MainContract.GeneratorModel
    {
        return RealSignalGenerator(context,settings)
    }
}
