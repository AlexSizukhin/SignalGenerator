package com.shokker.formsignaler


import android.app.Service
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shokker.formsignaler.model.MainContract
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class SimpleGenerator: MainContract.GeneratorModel, Service() {

    protected val TAG = "GeneratorService"

    private companion object{ var job : Job? = null
    }
    @Inject
    lateinit var mSettings : MainContract.GenerationSetting
    override var generationSettings: MainContract.GenerationSetting
        get() =  mSettings
        set(value) { mSettings = value  }

    protected var mGeneratingFunction : MainContract.SignalFunction? = null
    override var generatingFunction: MainContract.SignalFunction?
        get() = mGeneratingFunction
        set(value) { mGeneratingFunction = value}

    val mIsRunning =  MutableLiveData<MainContract.GenaratorStatus>()
    override val isRunning: LiveData<MainContract.GenaratorStatus>
        get() = mIsRunning

    protected  var buffersize:Int=-1
    protected  var samplerate:Int=-1
    var signalTick = Long.MIN_VALUE


    abstract suspend fun startAsync()
    ////////////////////////////////////////////////////////////////////////////////////
    protected var isRunningB = false
    override fun start() {
        if(job==null || job?.isActive!=true) {
            buffersize = mSettings.bufferSize
            samplerate = mSettings.frameRate
            job = GlobalScope.launch { startAsync() }
        }
        else
            Log.d(TAG, "Job is ${job.toString()}")
    }

    final override fun stop() {
        GlobalScope.launch {
            isRunningB = false
            Log.d(TAG, "About to stop ") }
    }

    protected fun fillBuffer(myArray: ShortArray, myFunction: (Double) -> Double)
    {
        val stepSize = 2.0*Math.PI/(samplerate/mGeneratingFunction!!.frequency)
        val amp = (mGeneratingFunction!!.ampletude/100.0*(0xFF/2)).toInt()
        for(i in 0 until myArray.size)
        {
            var vv:Double = myFunction.invoke((signalTick.rem(samplerate.toLong()).toDouble() * stepSize));
            if (vv>1) vv = 1.0
            if (vv<-1) vv = -1.0

            myArray[i] = swapBytes((((vv + 1.0) / 2.0) * amp).toInt())

            signalTick++
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    private fun swapBytes(s: Short):Short
    {
        var x1 = s.toInt() and (0x00FF) shl 8
        var x2 = s.toInt() and (0xFF00) shr 8
        return (x1+x2).toShort()
    }
    private fun swapBytes(s: Int):Short
    {
        var x1 = s and (0x00FF) shl 8
        var x2 = s and (0xFF00) shr 8
        return (x1+x2).toShort()
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}

