package com.shokker.formsignaler.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.shokker.formsignaler.model.MainContract
import com.shokker.formsignaler.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



/**
 * A simple [Fragment] subclass.
 * Use the [FunctionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FunctionFragment : Fragment(), MainContract.SignalFunctionView, MyNumberController.ChangeListener {

    @Inject
    lateinit var mFunctionPresenter: MainContract.SignalFunctionPresenter


    lateinit var mParametersLayout: LinearLayout

    lateinit var functionView: FunctionView
    lateinit var freqCtrl: MyNumberController
    lateinit var ampCtrl: MyNumberController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_function, container, false)
        freqCtrl = view.findViewById(R.id.freq)
        ampCtrl  = view.findViewById(R.id.ampletude)
        functionView = view.findViewById(R.id.functionView)
        mParametersLayout = view.findViewById(R.id.parametersLayout)
        return view
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    override fun showFunction(function: MainContract.SignalFunction) {
//        freqCtrl.clearOnChangedListeren()
//        ampCtrl.clearOnChangedListeren()
    freqCtrl.setOnChangedListeren(object: MyNumberController.ChangeListener {
        override fun OnChange(t: MyNumberController, value: Double) {
            function.frequency = value
        }
    })

    ampCtrl.setOnChangedListeren(object: MyNumberController.ChangeListener {
        override fun OnChange(t: MyNumberController, value: Double) {
            function.ampletude = value
        }
    })

        mFunctionPresenter.loadActiveFunction(function)
        functionView.mFunction = function::functionBody
        freqCtrl.currentValue = function.frequency
        ampCtrl.currentValue = function.ampletude

        functionView.invalidate()
        freqCtrl.invalidate()
        ampCtrl.invalidate()

        addParamControls(function)
    }
    private fun addParamControls(function: MainContract.SignalFunction)
    {
        mParametersLayout.removeAllViews()
        function.parameters.forEach {
            val control = MyNumberController(activity)
            control.currentValue = it.currentValue
            control.maxValue = it.maxValue
            control.minValue = it.minValue
            control.description = it.parameterName
            control.updateOnSeek = true
            control.invalidate()
            control.setOnChangedListeren(object: MyNumberController.ChangeListener {
                override fun OnChange(t: MyNumberController, value: Double) {
                    it.currentValue = value
                    functionView.invalidate()
//                    Log.d("Tag","On Change")
                }


            })

            mParametersLayout.addView(control)
        }
    }

    override fun OnChange(t: MyNumberController, value: Double) {

            }


}