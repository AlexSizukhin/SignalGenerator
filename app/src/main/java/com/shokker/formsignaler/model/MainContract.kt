package com.shokker.formsignaler.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

interface MainContract {
    enum class GenaratorStatus    {
        STOPPED ,        IS_RUNNING,        UNKNOWN     }
    /////////////////////////////// SETTINGS //////////////////////////////////
    interface GenerationSetting
    {
        var frameRate:Int
        var bufferSize:Int

        var startOnPlugCord : Boolean
        var stopOnUnPlugCord: Boolean
        var stopOnClose     : Boolean

    }
    interface GenerationSettingPresenter
    {
        fun loadSettings(): GenerationSetting
        fun saveSetting(setting: GenerationSetting)

        fun onSettingChange(newSetting: GenerationSetting)
    }
    interface GenerationSettingView
    {
        fun showSettings(settings: GenerationSetting)
        fun changeSettings(newSetting: GenerationSetting)
    }
    /////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// FUNCTION P-TRS ////////////////////////////
    interface FunctionParameter {
        val parameterName: String
        val minValue: Double
        val maxValue: Double
        var currentValue: Double
    }
    abstract class SignalFunction(){
        var frequency:Double = 50.0
        var ampletude:Double = 50.0

        abstract fun functionBody(x:Double):Double
        var parameters : MutableList<FunctionParameter> = mutableListOf()
        lateinit var functionName: String
        override fun toString(): String {
            return functionName
        }
    }
    interface SignalFunctionPresenter
    {
        var currentSignalFunction: SignalFunction?

        fun loadActiveFunction(signalFunction: SignalFunction)
        fun saveActiveFunction()
    }
    interface SignalFunctionView
    {
        fun showFunction(function : SignalFunction)
    }
/////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// GENERATOR ////////////////////////////////////////
    interface GeneratorModel
    {
        suspend fun start()
        fun stop()
        var generationSettings: GenerationSetting
        var generatingFunction: SignalFunction?
        val isRunning: LiveData<GenaratorStatus>
    }

    interface GeneratorPresenter
    {
        fun start()
        fun stop()
        fun setSettings(settings: GenerationSetting)
        fun setGenerationFunction(signalFunction: SignalFunction)

        val isRunning : GenaratorStatus

        fun onActivate(lcOwner: LifecycleOwner)
        fun onDeactivate()
    }
    interface GeneratorView
    {
        fun start()
        fun stop()
        fun onStatusChange(newStatus: GenaratorStatus)
        fun setStatus(status : GenaratorStatus)

        fun onActivate()
        fun onDeativate()
    }
}
