package com.shokker.formsignaler

import androidx.room.Room
import com.shokker.formsignaler.data.SignalGeneratorDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import android.content.Context
import android.util.Log
import com.shokker.formsignaler.data.SettingsDataDao
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule{
    @Provides
    fun provideDatabase(@ApplicationContext appContext:Context): SignalGeneratorDB
    {
        return Room.databaseBuilder(
            appContext,SignalGeneratorDB::class.java,
            "signal_generator_db"
        ).build()
    }
}*/

@InstallIn(SingletonComponent::class)
@Module
class DAOModule
    constructor(){
    var appContext:Context? =null

        val db:SettingsDataDao by lazy {
        Room.databaseBuilder(
                appContext!!,SignalGeneratorDB::class.java,
                "signal_generator.db"
        ).build().settingsDao()
    }
    @Provides
    @Singleton
    fun DAOProvide(@ApplicationContext context:Context):SettingsDataDao
    {
        Log.d("HILT","generating DaoProvider")
        appContext = context
        return db
    }
}

@InstallIn(SingletonComponent::class)
@Module
class SettingsModule {
    var appContext:Context? =null
    val settingsDao : SettingsDataDao by lazy {
        Room.databaseBuilder(
                appContext!!,SignalGeneratorDB::class.java,
                "signal_generator.db"
        ).allowMainThreadQueries().build().settingsDao()
    }
    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext context:Context): MainContract.GenerationSetting
    {
        appContext = context
        val s=  settingsDao.getSettings()
        if(s!=null) {
            Log.d("HILT", "settings found")
            settingsDao.getAllSettings().forEach(){
                Log.d("ROOM", it.toString())
            }
        }
        else
            return GenerationSettingsImpl()
        return  s
    }

}

class GenerationSettingsImpl() : MainContract.GenerationSetting {

    init {
        Log.d("HILT","Creating settings object (nothing found in db")
    }

    override var frameRate: Int = 22050

    override var bufferSize: Int=5000

    override var startOnPlugCord=  true

    override var stopOnUnPlugCord= true

    override var stopOnClose= false




}