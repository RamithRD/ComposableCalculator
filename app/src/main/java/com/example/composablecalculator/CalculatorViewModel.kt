package com.example.composablecalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
        }
    }

    private fun performDeletion() {
        when {
            state.operand2.isNotBlank() -> state = state.copy(
                operand2 = state.operand2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation =  null
            )
            state.operand1.isNotBlank() -> state = state.copy(
                operand1 = state.operand1.dropLast(1)
            )
        }
    }

    private fun performCalculation() {
        val operand1 = state.operand1.toDoubleOrNull()
        val operand2 = state.operand2.toDoubleOrNull()
        if(operand1 != null && operand2 != null) {
            val result = when(state.operation) {
                is CalculatorOperation.Add -> operand1 + operand2
                is CalculatorOperation.Subtract -> operand1 - operand2
                is CalculatorOperation.Multiply -> operand1 * operand2
                is CalculatorOperation.Divide -> operand1 / operand2
                null -> return
            }
            state = state.copy(
                operand1 = result.toString().take(15),
                operand2 = "",
                operation = null
            )
        }


    }

    private fun enterOperation(operation: CalculatorOperation) {
        if(state.operand1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if(state.operation == null && !state.operand1.contains(".")
            && state.operand1.isNotBlank()
        ) {
           state = state.copy(
               operand1 = state.operand1 + "."
           )
            return
        }
        if(!state.operand2.contains(".") && state.operand2.isNotBlank()) {
            state = state.copy(
                operand2 = state.operand2 + "."
            )
        }
    }

    private fun enterNumber(number: Int) {
        if(state.operation == null) {
            if(state.operand1.length >= MAX_NUM_LEN) {
                return
            }
            state = state.copy(
                operand1 = state.operand1 + number
            )
            return
        }
        if(state.operand2.length >= MAX_NUM_LEN) {
            return
        }
        state = state.copy(
            operand2 = state.operand2 + number
        )
    }

    companion object {
        private const val MAX_NUM_LEN = 8
    }

}