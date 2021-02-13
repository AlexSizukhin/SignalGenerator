package com.shokker.formsignaler


import androidx.lifecycle.ViewModel
import com.shokker.formsignaler.data.GeneralRepository
import com.shokker.formsignaler.model.MainContract
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SettingsViewModel
@Inject
    constructor(val repository:GeneralRepository): ViewModel(), MainContract.GenerationSettingPresenter{
    init{

    }

@Inject
    lateinit var mModel: MainContract.GenerationSetting

    override fun loadSettings(): MainContract.GenerationSetting {
        //val s = repository.readAllSettings //.value



        return mModel //s?:
    }

    override fun saveSetting(setting: MainContract.GenerationSetting) {
        repository.updateSettins(setting)



    }

    override fun onSettingChange(newSetting: MainContract.GenerationSetting) {
        TODO("Not yet implemented")
    }
    // TODO: Implement the ViewModel
}