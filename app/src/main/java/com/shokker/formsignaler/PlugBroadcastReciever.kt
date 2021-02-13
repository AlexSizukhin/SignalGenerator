package com.shokker.formsignaler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class PlugBroadcastReciever: BroadcastReceiver(){

    private val TAG = "BroadcastReceiver"

    @Inject
   // lateinit var genPres:  GeneratorPresenterImpl // MainContract.GeneratorPresenter
    lateinit var generatorViewModel: GeneratorViewModel

    private val service: MainContract.GeneratorModel?
    get(){
        try {
            return generatorViewModel.generatorServiceBinder.value?.getService()
        }catch (e:Exception){ Log.d(TAG,e.toString()) }

        return null
    }

    private val status: MainContract.GenaratorStatus
    get(){
        return service?.isRunning?.value?: MainContract.GenaratorStatus.UNKNOWN
        //return MainContract.GenaratorStatus.UNKNOWN
    }
    private val settings: MainContract.GenerationSetting?
        get(){
            return service?.generationSettings
            //return MainContract.GenaratorStatus.UNKNOWN
        }

    override fun onReceive(context: Context?, intent: Intent?) {
        // AIRPLANE MODE TO TEST BroadcastReciever
        if (intent?.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            Log.d(TAG,"AVIA")       // for test
            val state = intent!!.getBooleanExtra("state",false)
                if(state)
                    onPlug()
                else
                    onUpPlug()
            }


        if (intent?.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            val state: Int = intent!!.getIntExtra("state", -1)
            when (state) {
                0 -> onUpPlug()
                1 -> onPlug()
                else -> Log.d(TAG, "Error")
            }
        }
    }

    private fun onPlug(){
        Log.d(TAG, "Headset plugged")
        if(status == MainContract.GenaratorStatus.STOPPED && settings?.startOnPlugCord?:false)
            generatorViewModel.start()
    }
    private fun onUpPlug()
    {
        Log.d(TAG, "Headset unplugged")
        if(status == MainContract.GenaratorStatus.IS_RUNNING && settings?.stopOnUnPlugCord?:false)
            generatorViewModel.stop()

    }
}