package com.shokker.formsignaler.UI

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shokker.formsignaler.*
import com.shokker.formsignaler.model.MainContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //private lateinit var fullscreenContentControls: LinearLayout

    lateinit var startButton:FloatingActionButton
    lateinit var settingsButton:FloatingActionButton
    lateinit var functionSpinner:Spinner
    lateinit var mainFragment: FunctionFragment

//    @Inject
//    lateinit var genPres : GeneratorPresenterImpl//MainContract.GeneratorPresenter //

    @Inject
    lateinit var functionFactory: FunctionFactory

    @Inject
    lateinit var generatorViewModel: GeneratorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        disableSounds(findViewById<ViewGroup>(R.id.mainLayout).rootView as ViewGroup)

        startButton = findViewById(R.id.playFloatButton)
        settingsButton=findViewById(R.id.settingsFloatButton)
        functionSpinner=findViewById(R.id.functionList)

        mainFragment = supportFragmentManager.findFragmentById(R.id.mainfragment) as FunctionFragment

        startButton.setOnClickListener(::onStartClick)
        settingsButton.setOnClickListener(::onSettingsOkButton)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.hide()

        val adapter =  functionFactory.functionArrayAdapter
        functionSpinner.adapter = adapter

        generatorViewModel.generatorStatus.observe(this,{ Log.d("MainActivity"," generator status changed ${it}"); onChangeService(it) })
        generatorViewModel.generatorServiceBinder.observe(this,{
            if(it==null){
                Log.d("Activity","generatorServiceBinder is null")
                return@observe }
            if(generatorViewModel.generationFunction==null){
                Log.d("Activity","generation function is null")
                return@observe }
            Log.d("Activity", "Is running. Must recover settings")
            functionSpinner.setSelection(adapter.getPosition(generatorViewModel.generationFunction))


        })
        generatorViewModel.onActivate()
        functionSpinner.onItemSelectedListener = this
    }

    override fun onResume() {
        super.onResume()

    }

    fun onChangeService(status: MainContract.GenaratorStatus){
        Log.d("TAG", "${status}")
        if(status == MainContract.GenaratorStatus.IS_RUNNING)
            startButton.setImageResource(R.drawable.ic_baseline_pause_24)
        if(status != MainContract.GenaratorStatus.IS_RUNNING)
            startButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    override fun onStop() {
       // generatorViewModel.onDeactivate()         // not sure if it is required
        super.onStop()
    }

    fun onStartClick(v: View?)
    {
        if(generatorViewModel.generatorStatus.value != MainContract.GenaratorStatus.IS_RUNNING)
            generatorViewModel.start()
        else
            generatorViewModel.stop()
    }
    fun onSettingsOkButton(v: View?)
    {
        //Toast.makeText(this,"Close settings",Toast.LENGTH_SHORT).show()
        val newFragment: SettingsDialog = SettingsDialog.newInstance()
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.myDialog)
        newFragment.show(supportFragmentManager, "")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("nothing selected")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d("Frag", "On Item ${p2} selected")
        val selectedFunction = (functionSpinner.adapter!!.getItem(p2) as MainContract.SignalFunction)
        Log.d("Frag","new selected ${selectedFunction.ampletude.toString()}")

        mainFragment.showFunction(selectedFunction)
        if(! generatorViewModel.generatorServiceBinder.hasActiveObservers())
            generatorViewModel.generatorServiceBinder.observe(this, {
                it.getService().generatingFunction = selectedFunction
            })
        generatorViewModel.generationFunction = selectedFunction
        Toast.makeText(this, "Selected ${selectedFunction.functionName}", Toast.LENGTH_SHORT).show()
    }


    private fun disableSounds(v: ViewGroup)
    {
        for(i in 0..v.childCount-1)
        {
            var nv = v.getChildAt(i)
            if(nv==null)
                continue
            nv.isSoundEffectsEnabled = false
            if(nv is ViewGroup)
                disableSounds(nv)
        }
    }
}