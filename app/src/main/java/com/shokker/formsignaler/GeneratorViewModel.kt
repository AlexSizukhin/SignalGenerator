package com.shokker.formsignaler

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneratorViewModel
    @Inject constructor(@ApplicationContext val appContext: Context)
    :        ViewModel() {
    private val TAG = "Generator ViewModel"
    init { Log.d(TAG,"ViewModel for Generator created ctor/init") }

    protected val serviceClass: Class<GeneratorService> = GeneratorService::class.java

    private  val mGeneratorStatus: MutableLiveData<MainContract.GenaratorStatus> = MutableLiveData()
    val generatorStatus: LiveData<MainContract.GenaratorStatus>
        get() = mGeneratorStatus


    private val mGeneratorServiceBinder: MutableLiveData<GeneratorService.SignalGeneratorBinder> = MutableLiveData()
    val generatorServiceBinder: LiveData<GeneratorService.SignalGeneratorBinder>
        get() = mGeneratorServiceBinder


    var generationFunction:MainContract.SignalFunction?                  // todo convert to LiveData
        get () =  mGeneratorServiceBinder.value?.getService()?.generatingFunction
        set(value) { mGeneratorServiceBinder.value?.getService()?.generatingFunction = value}

    var genetationSettings:MainContract.GenerationSetting               // todo convert to LiveData
        get() = service.generationSettings
        set(value) { service.generationSettings = value }


    private val service : GeneratorService  get() = mGeneratorServiceBinder.value!!.getService()

    fun start()
    {
        service.start()
    }
    fun stop()
    {
        service.stop()
    }

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "onService: connected to service")
            mGeneratorServiceBinder.postValue(binder as  GeneratorService.SignalGeneratorBinder)
//            if(mSignalFunction!=null)
//                binder.getService().generatingFunction = mSignalFunction!!
            binder.getService().isRunning.observeForever { mGeneratorStatus.postValue(it) }

        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            Log.d(TAG, "Service unbinded")
            mGeneratorServiceBinder.postValue(null)

        }
    }

    fun onActivate()
    {

        val intent = Intent(appContext,serviceClass)        //  GeneratorService::class.java
        appContext.startService(intent)
        appContext.bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE)
    }
    fun onDeactivate()
    {
        try {
            appContext.unbindService(serviceConnection)
        }catch (e:Exception) { Log.d(TAG,e.toString())}

        Log.d(TAG,"onDeactivate")
        try {
            val b= mGeneratorServiceBinder.value as GeneratorService.SignalGeneratorBinder
            if(b.getService().generationSettings.stopOnClose==true)
                b.getService().stop()
        }catch (e:Exception){ Log.d(TAG,e.toString()) }

    }
}