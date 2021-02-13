package com.shokker.formsignaler.UI


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.shokker.formsignaler.R
import com.shokker.formsignaler.SettingsViewModel
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsDialog : DialogFragment(),View.OnClickListener, MainContract.GenerationSettingView {

    companion object {
        fun newInstance() = SettingsDialog()
    }

    @Inject
    lateinit var viewModel: SettingsViewModel
    @Inject
    lateinit var mSettings: MainContract.GenerationSetting

    lateinit var bufferSize:EditText
    lateinit var sampleRate:Spinner
    lateinit var swOnPlug:SwitchCompat
    lateinit var swOnUnPlug:SwitchCompat
    lateinit var swOnClose:SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(R.string.settings)

        val v = inflater.inflate(R.layout.settings_fragment, container, false)

        bufferSize = v.findViewById(R.id.editTextBufferSize)
        sampleRate = v.findViewById(R.id.spinnerSampleRate)
        swOnClose  = v.findViewById(R.id.swStopOnClose)
        swOnPlug = v.findViewById(R.id.swStartOnPlug)
        swOnUnPlug= v.findViewById(R.id.swStopOnUnplug)

        val okButton: Button = v.findViewById(R.id.okSettingsButton) as Button
        okButton.setOnClickListener(this)
        val cancelButton : Button = v.findViewById(R.id.cancelSettingsButton) as Button
        cancelButton.setOnClickListener({ this.dismiss()
          //  Toast.makeText(activity, R.string.settings_not_changed ,Toast.LENGTH_SHORT).show()
        })

        mSettings = viewModel.loadSettings()
        showSettings(mSettings)
        return v
    }


    override fun onClick(p0: View?) {

            mSettings.stopOnUnPlugCord = swOnUnPlug.isChecked

            mSettings.startOnPlugCord =  swOnPlug.isChecked

            mSettings.stopOnClose =  swOnClose.isChecked

        mSettings.bufferSize = bufferSize.getText()!!.toString().toInt()
        mSettings.frameRate =sampleRate.selectedItem.toString().toInt()

        viewModel.saveSetting(mSettings)

        this.dismiss()
    //    Toast.makeText(activity,R.string.settings_changed,Toast.LENGTH_SHORT).show()

    }
///////////////////////////////////////////////////////////////////////////////////////////

    override fun showSettings(settings: MainContract.GenerationSetting) {
        swOnUnPlug.isChecked = settings.stopOnUnPlugCord
        swOnPlug.isChecked = settings.startOnPlugCord
        swOnClose.isChecked = settings.stopOnClose

        bufferSize.setText(settings.bufferSize.toString())
        sampleRate.setSelection(setValueToSpinner(sampleRate.adapter!!,settings.frameRate))                  // TODO make with adapter
    }

    private fun setValueToSpinner(spinnerAdapter: SpinnerAdapter ,frameRate:Int):Int
    {
        for(i in 0..spinnerAdapter.count-1)
            if(spinnerAdapter.getItem(i)==frameRate.toString())
                return i
        return  0
    }

    override fun changeSettings(newSetting: MainContract.GenerationSetting) {
        TODO("Not yet implemented")
    }


}