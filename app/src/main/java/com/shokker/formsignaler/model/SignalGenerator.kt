package com.shokker.formsignaler.model


import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RealSignalGenerator
    :MainContract.GeneratorModel {

    protected val TAG = "Real Signal Generator"


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


    protected fun prepare():AudioTrack
    {
        TODO()
    }
    ////////////////////////////////////////////////////////////////////////////////////
    private var isRunningB = false
    override suspend fun start() {
        val audioTrack = prepare()
        val signalBuffer = ShortArray(mSettings.bufferSize)
        while (isRunningB)
        {
            fillBuffer(signalBuffer,mGeneratingFunction!!::functionBody)
            audioTrack.write(signalBuffer,0,signalBuffer.size)
        }
    }

    override fun stop() {
        isRunningB = false
    }

    private fun fillBuffer(myArray: ShortArray, myFunction: (Double) -> Double)
    {
        val stepSize = 2.0*Math.PI/(samplerate/mGeneratingFunction!!.frequency)
        val amp = (mGeneratingFunction!!.ampletude/100.0*(0xFF/2)).toInt()
        for(i in 0 until myArray.size)
        {
            var vv:Double = this.mGeneratingFunction!!.functionBody(signalTick.rem(samplerate.toLong()).toDouble() * stepSize);
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