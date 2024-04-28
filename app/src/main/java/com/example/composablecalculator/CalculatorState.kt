package com.example.composablecalculator

data class CalculatorState(
    val operand1: String = "",
    val operand2: String = "",
    val operation: CalculatorOperation? = null,
)
