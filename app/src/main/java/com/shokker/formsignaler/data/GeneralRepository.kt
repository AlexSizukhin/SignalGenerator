package com.shokker.formsignaler.data

import android.util.Log
import com.shokker.formsignaler.model.MainContract
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralRepository
@Inject
    constructor(private val settingsDataDao: SettingsDataDao) {
    var readAllSettings:SettingsData? = null
    init {
        GlobalScope.launch(Dispatchers.IO)
        {
            readAllSettings = settingsDataDao.getSettings()
            Log.d("GeneralRep","Loading")
            settingsDataDao.getAllSettings().forEach(){
                Log.d("Room",it.toString())
            }
        }
     }/* : LiveData<SettingsData>*/


    fun updateSettins(settings: MainContract.GenerationSetting)
    {
        GlobalScope.launch(Dispatchers.IO)
        {
            val s = SettingsData(13,
                settings.frameRate,
                settings.bufferSize,
                settings.startOnPlugCord,
                settings.stopOnUnPlugCord,
                settings.stopOnClose
            )
            settingsDataDao.addSettings(s)
        }
    }

}