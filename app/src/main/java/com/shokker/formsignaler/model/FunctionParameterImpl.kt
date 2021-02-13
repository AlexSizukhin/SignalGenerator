package com.shokker.formsignaler.model

import com.shokker.formsignaler.model.MainContract

class FunctionParameterImpl(
    override val parameterName: String, override val minValue:
    Double, override val maxValue: Double, override var currentValue: Double
) : MainContract.FunctionParameter {
}