package com.shokker.formsignaler.model


import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RealSignalGenerator
    @Inject
    constructor()
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

    ////////////////////////////////////////////////////////////////////////////////////
    protected fun prepare(bufferSize:Int, samplerate:Int):AudioTrack
    {
        val audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, samplerate, android.media.AudioFormat.CHANNEL_OUT_MONO,
                android.media.AudioFormat.ENCODING_PCM_16BIT, bufferSize*2,
                AudioTrack.MODE_STREAM)
        audioTrack.setVolume(AudioTrack.getMaxVolume())
        audioTrack.play()
        return  audioTrack
    }
    protected fun cleanUp(audioTrack: AudioTrack?)
    {
        audioTrack?.flush();
        audioTrack?.stop();
        audioTrack?.release();
    }
    ////////////////////////////////////////////////////////////////////////////////////
    private var isRunningB = false
    override suspend fun start() {
        var audioTrack: AudioTrack? = null
        try{
            val bufferSize = mSettings.bufferSize
            val frameRate = mSettings.frameRate
            audioTrack = prepare(bufferSize,frameRate)
            val signalBuffer = ShortArray(bufferSize)
            mIsRunning.postValue(MainContract.GenaratorStatus.IS_RUNNING)
            while (isRunningB)                                                          // todo Add watchdog here or in stervice
            {
                fillBuffer(signalBuffer,mGeneratingFunction!!,frameRate)
                audioTrack.write(signalBuffer,0,signalBuffer.size)
            }
        }catch (e:Exception) { Log.e(TAG,e.toString())}
        finally {
            try {
                cleanUp(audioTrack)
                mIsRunning.postValue(MainContract.GenaratorStatus.STOPPED)
            }catch (e:Exception) { Log.e(TAG,e.toString()); mIsRunning.postValue(MainContract.GenaratorStatus.UNKNOWN) }

        }
    }

    override fun stop() {
        isRunningB = false
    }

    ////////////////////////////////////////////////////////////////////////////////////
    private var signalTick = Long.MIN_VALUE
    private fun fillBuffer(myArray: ShortArray, function: MainContract.SignalFunction,samplerate : Int)
    {
        val stepSize = 2.0*Math.PI/(samplerate/function.frequency)
        val amp = (function.ampletude/100.0*(0xFF/2)).toInt()
        for(i in 0 until myArray.size)
        {
            var vv:Double = function.functionBody(signalTick.rem(samplerate.toLong()).toDouble() * stepSize);
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