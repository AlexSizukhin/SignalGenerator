package com.shokker.formsignaler.UI

import android.view.View
import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.shokker.formsignaler.R
import kotlin.math.sin
import kotlin.math.PI

class FunctionView : View
{

    var mFunction: (Double)->Double = ::sin
    var stepCount = 100
    var stepSize = 2*PI/stepCount

    constructor(context: Context?):super(context)
    {
        init(null)
    }
    constructor(context: Context?, attrs: AttributeSet?):super(context, attrs)
    {
        init(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr)
    {
        init(attrs)
    }

    private   var  lineThikness=3.0f
    private  var lineColor = Color.RED

    private fun init(set: AttributeSet?)
    {
        context.theme.obtainStyledAttributes(
            set,
                R.styleable.FunctionView,
            0, 0).apply {

            try {
                lineThikness = getFloat(R.styleable.FunctionView_lineThikness, 3.0f)
                lineColor = getColor(R.styleable.FunctionView_lineColor, lineColor )
                stepCount = getInt(R.styleable.FunctionView_stepCount,100 )
            } finally {
                recycle()
            }
        }

    }

    override protected  fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        if(canvas==null)
            return
        val h =canvas.height-lineThikness*2
        val w =canvas.width
        var x1:Double =0.0
        var x2:Double
        var y1=-mFunction.invoke(x1)*h/2.0+h/2.0+lineThikness
        var y2:Double
        var p = Paint()
        p.strokeWidth = lineThikness
        p.color = lineColor
        while (x1<2*PI) {
            x2 =x1+(stepSize)
            y2=-mFunction.invoke(x1)*h/2.0+h/2.0+lineThikness
            canvas.drawLine((x1/(2*PI) *w).toFloat(),y1.toFloat(),(x2/(2*PI)*w).toFloat(),y2.toFloat(),p)
            x1 = x2
            y1 = y2
        }
    }
}