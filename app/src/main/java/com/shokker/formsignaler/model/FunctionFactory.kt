package com.shokker.formsignaler

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import com.shokker.formsignaler.model.FunctionParameterImpl
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.PI
import kotlin.random.Random

@Singleton
class FunctionFactory
@Inject constructor()
{
    init {
        Log.d("FunFac","creating FunctionFactory")
    }
    @ApplicationContext
    @Inject
    lateinit var context: Context

    val functionArrayAdapter: ArrayAdapter<MainContract.SignalFunction> by lazy{
        ArrayAdapter<MainContract.SignalFunction>(
                context,
                R.layout.my_spinner_row,// simple_spinner_dropdown_item , //2131492976
                //getFunctionArray()
                functionArray
        )
    }


    private  val functionArray:  Array<MainContract.SignalFunction> by lazy {
        arrayOf(SinFunction(),NoiseFunction(),PWMFunction(),DoubleSidePWM(),SAWFinction())
    }

    inner class SinFunction : MainContract.SignalFunction() {
        init {
            functionName = context.getString(R.string.sin_function_name)
        }
        override fun functionBody(x: Double): Double = Math.sin(x)
    }
    inner class NoiseFunction : MainContract.SignalFunction() {
        init {
            functionName = context.getString(R.string.noise_function_name)

        }
        override fun functionBody(x: Double): Double = Random.nextDouble()*2.0-1.0
    }
    inner class PWMFunction: MainContract.SignalFunction()
    {
        val step =  FunctionParameterImpl(context.getString(R.string.step_proc_param),0.0,100.0,10.0)
        init{
            functionName =   context.getString(R.string.pwm_function_name)// "PWM (Pulse-width modulation)"       // todo resource strings
            parameters.add(step)
        }
        override fun functionBody(x: Double): Double {
            if (x.rem(2.0 * PI) < step.currentValue / 100.0 * 2.0 * PI)
                return -1.0
            else return 1.0
        }
    }

    inner class SAWFinction: MainContract.SignalFunction()
    {
        val angle =  FunctionParameterImpl(context.getString(R.string.ang_param),0.0001,100.0,10.0)
        init {
            functionName = context.getString(R.string.saw_function_name)
            parameters.add(angle)
        }
        override fun functionBody(x: Double): Double {
            val k1 = 1.0 / (PI / (angle.currentValue + 1))
            val k2 = 1.0 / (PI - PI / (angle.currentValue + 1))
            return if (k1 * x < 1.0)
                k1 * x
            else if (k2 * (PI - x) > -1.0)
                k2 * (PI - x)
            else
                k1 * (x - 2 * PI)
        }

    }
    inner class DoubleSidePWM : MainContract.SignalFunction()
    {
        val stepA =  FunctionParameterImpl(context.getString(R.string.step_up_param),0.0,50.0,10.0)
        val stepB =  FunctionParameterImpl(context.getString(R.string.step_down_param),0.0,50.0,10.0)
        init {
            functionName = context.getString(R.string.double_pwm_function_name)
            parameters.add(stepA)
            parameters.add(stepB)
        }
        override fun functionBody(x: Double): Double {
            return if (x.rem(2.0 * PI) < stepA.currentValue / 100.0 * 2.0 * PI)
                1.0
            else if ((x.rem(2.0 * PI) > PI) && (x.rem(2.0 * PI) - PI < stepB.currentValue / 100.0 * 2.0 * PI))
                -1.0
            else 0.0
        }
    }

}



