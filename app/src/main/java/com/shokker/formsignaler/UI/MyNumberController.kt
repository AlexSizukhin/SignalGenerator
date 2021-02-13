package com.shokker.formsignaler.UI

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.*
import com.shokker.formsignaler.R
import java.util.*
import kotlin.math.absoluteValue

class MyNumberController : LinearLayout,  EventListener {

    constructor(context: Context?):super(context)
    {
        init(null,null)
    }
    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs)
    {
        init(attrs,null)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr)
    {
        init(attrs,defStyleAttr)
    }

    lateinit var editText:EditText
    lateinit var seekBar: SeekBar
    var textView: TextView? = null
    private var mDescription: String = ""
    var description: String
    get() { return mDescription }
    set(value){
        mDescription = value
        textView?.text = value
    }
    var updateOnSeek = false
    var isSeekPressed = false
    private var pCurrentValue: Double = 0.0
    var currentValue: Double
    get() { return pCurrentValue}
    set(value) {
        pCurrentValue = value
        if(value>maxValue) pCurrentValue=maxValue
        if(value<minValue) pCurrentValue=minValue

        mChangedFunction?.OnChange (this,value)
    }

    var minValue: Double = Double.MIN_VALUE
    var maxValue: Double = Double.MAX_VALUE

    override fun invalidate() {
        editText.setText(currentValue.toString())
        textView?.setText(description)
        super.invalidate()
    }

    private fun init(set:AttributeSet?,defStyleAttr:Int?) {
        parseXMLParameters(set)
        orientation = VERTICAL

        if(defStyleAttr==null)
            editText = EditText(context,null)
        else
            editText = EditText(context,null,defStyleAttr)

        editText.inputType = InputType.TYPE_CLASS_NUMBER

        editText.setText(currentValue.toString())

        if(defStyleAttr==null)
            seekBar = SeekBar(context,null)
        else
            seekBar = SeekBar(context,null,defStyleAttr)

        seekBar.max = 100
        seekBar.progress = 50

        if(defStyleAttr==null)
            textView = TextView(context)
        else
            textView = TextView(context,null,defStyleAttr)

        textView?.text = description

        addView(textView)
        addView(editText)
        addView(seekBar)


        seekBar.setOnSeekBarChangeListener(MySeekChange(this.context,editText,this))
      // editText.addTextChangedListener(MyTextWatcher(this.context,this))

        editText.setOnFocusChangeListener { view, b ->
            if(!b)
                (view.parent as MyNumberController).currentValue = (view as TextView).text.toString().toDouble()
//            Log.d("a","focusChange ${b.toString()} ${(view.parent as View).id}")
        }


    }

    var mChangedFunction: ChangeListener? = null
    fun setOnChangedListeren(f: ChangeListener)
    {
        mChangedFunction  = f
    }
    fun clearOnChangedListeren()
    {
        mChangedFunction = null
    }

    fun formatNumberToText(value: Double):String
    {
        if(value.absoluteValue<10.0)
            return value.toString()
        return Math.ceil(value).toString()
    }

    private fun parseXMLParameters(set: AttributeSet?)
    {
        context.theme.obtainStyledAttributes(
                set,
                R.styleable.MyNumberController,
                0, 0) .apply {

            try {
                minValue = getFloat(R.styleable.MyNumberController_minValue, Float.MIN_VALUE).toDouble()
                maxValue = getFloat(R.styleable.MyNumberController_maxValue, Float.MAX_VALUE).toDouble()
                val tCurVal = minValue+maxValue /2.0
                currentValue = getFloat(R.styleable.MyNumberController_currentValue, tCurVal.toFloat()).toDouble()
                updateOnSeek = getBoolean(R.styleable.MyNumberController_updateOnSeek,false )
                description = getString(R.styleable.MyNumberController_description)?:""
            } finally {
                recycle()
            }
        }
    }

    class MySeekChange(c1: Context?,t1: EditText,m : MyNumberController) : SeekBar.OnSeekBarChangeListener
    {
        val mCtrl : MyNumberController = m
        val c: Context? = c1
        val tE: EditText = t1
        var bVal: Double =0.0

        fun oldChangeValueByFormula(baseValeu:Double,progressVal: Int):Double
        {       // TODO make smart!
            var k:Double
            k = progressVal/50.0
            return   (baseValeu*k)
        }
        fun ChangeValueByFormula(baseValeu:Double,progressVal: Int):Double
        {
            var normalizedBias = (progressVal-50.0)/50.0
            val positiveDest = mCtrl.maxValue - baseValeu
            val negativeDest = mCtrl.minValue - baseValeu
            // linear
            if(normalizedBias>0)
                return baseValeu + positiveDest*Math.pow(normalizedBias,3.0)
            else
                return baseValeu - negativeDest*Math.pow(normalizedBias,3.0)
        }
        //
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            if(p2) {
                val cV = ChangeValueByFormula(bVal, p0?.progress!!)
                tE.setText(
                        mCtrl.formatNumberToText(cV))
                if(mCtrl.updateOnSeek)
                    mCtrl.currentValue = cV

            }
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
            mCtrl.isSeekPressed = true
            bVal = mCtrl.currentValue
        }

        override fun onStopTrackingTouch(p0: SeekBar) {
            mCtrl.currentValue = ChangeValueByFormula(bVal,p0.progress)
            mCtrl.isSeekPressed = false
            tE.setText(mCtrl.formatNumberToText(mCtrl.currentValue))
            p0.progress = 50
        }

    }

    interface  ChangeListener
    {
        fun OnChange(t: MyNumberController, value:Double)
        {
        }
    }


    }