package com.shokker.formsignaler

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import com.shokker.formsignaler.UI.MainActivity
import com.shokker.formsignaler.model.MainContract
import com.shokker.formsignaler.model.RealSignalGenerator


import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
@Singleton
class GeneratorService: Service(), MainContract.GeneratorModel
    /*:  SimpleGenerator() */{
    // no inheritance. yes to composition
    protected val TAG = "GeneratorService"
    init {
        Log.d(TAG, "Service ctor")
    }


    @Inject
    lateinit var signalGenerator: RealSignalGenerator

    private companion object{var job : Job? = null
        val CHANNEL_ID = "generationService"
    }


    private var mBinder : IBinder = SignalGeneratorBinder()


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onCreate() {
        Log.d(TAG, "OnCreate (service)")

        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "On StartCmd")
//        val input = intent.getStringExtra("inputExtra")

        val notIntent = Intent(this, MainActivity::class.java)
        val pendIntent = PendingIntent.getActivity(this,
                0, notIntent, 0)

        val channel: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) channel = createNotificationChannel(getString(R.string.app_name), CHANNEL_ID) else {
            channel = CHANNEL_ID
        }

        val notification = NotificationCompat.Builder(this,
                channel)
            .setContentTitle(getString(R.string.title_activity_main))
            .setContentText(getString(R.string.to_return))          // todo resources
            .setSmallIcon(R.drawable.icon)// ic_baseline_waves_24)
            .setContentIntent(pendIntent)
            .build()

        startForeground(1, notification)
        return START_NOT_STICKY
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stop()
        stopSelf()
    }

    inner class SignalGeneratorBinder : Binder() {
        fun getService():GeneratorService
        {
            return this@GeneratorService
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////
    override suspend fun start() {                      // todo change coroutine scope to IO
        if(job ==null || job?.isActive!=true) {         //// todo Add watchdog here or in realGenerator
           job = CoroutineScope(Dispatchers.IO).launch { signalGenerator.start() }
        }
        else {
            Log.d(TAG, "Job is ${job.toString()}")
            throw Exception("JOB IS STILL RUNNING")         // temporary exception, convert to stop and start
        }
    }

    override fun stop() {
            signalGenerator.stop()
            Log.d(TAG, "About to stop ")
    }

    override var generationSettings: MainContract.GenerationSetting
        get() = signalGenerator.generationSettings
        set(value) { signalGenerator.generationSettings = value }
    override var generatingFunction: MainContract.SignalFunction?
        get() = signalGenerator.generatingFunction
        set(value) { signalGenerator.generatingFunction = value}
    override val isRunning: LiveData<MainContract.GenaratorStatus>
        get() = signalGenerator.isRunning


}
