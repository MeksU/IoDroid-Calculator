package pl.meksu.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private var tvInput: TextView? = null
    private var num1: Double = 0.0
    private var operationSign: String = ""
    private var operationClicked: Boolean = false
    private var onEqualClicked: Boolean = false
    private var lastNumeric: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvInput = findViewById(R.id.tvInput)
    }

    fun onDigit(view: View) {
        if(!operationClicked) {
            if (tvInput?.text.toString().length < 9) {
                lastNumeric = true

                if (tvInput?.text.toString() == "0")
                    tvInput?.text = (view as Button).text
                else if (tvInput?.text.toString() == "-0")
                    tvInput?.text = "-${(view as Button).text}"
                else
                    tvInput?.append((view as Button).text)
            }
        }
        else {
            tvInput?.text = (view as Button).text
            operationClicked = false
            lastNumeric = true
        }
    }

    fun onClear(view: View) {
        tvInput?.text = "0"
        operationClicked = false
        operationSign = ""
        lastNumeric = false
    }

    fun onPlusMinus(view: View) {
        if(operationClicked) {
            tvInput?.text = "-0"
            operationClicked = false
        }
        else {
            if(tvInput?.text.toString().contains("-")) {
                val currentText = tvInput?.text.toString()
                if (currentText.startsWith("-")) {
                    tvInput?.text = currentText.substring(1)
                }
                val stringInput = tvInput?.text.toString()
                num1 = stringInput.toDouble()
            } else {
                tvInput?.text = "-${tvInput?.text.toString()}"
                val stringInput = tvInput?.text.toString()
                num1 = stringInput.toDouble()
            }
        }
    }

    fun onPercent(view: View) {
        val stringInput = tvInput?.text.toString()
        val number = stringInput.toDouble() / 100
        tvInput?.text = number.toString()
    }

    fun onDecimalPoint(view: View) {
        if(!tvInput?.text.toString().contains("."))
            tvInput?.append(".")
    }

    fun onOperation(view: View) {
        if(operationSign == "" && lastNumeric) {
            operationClicked = true
            operationSign = (view as Button).text.toString()
            val stringInput = tvInput?.text.toString()
            num1 = stringInput.toDouble()
            onEqualClicked = false
        }
        else if(lastNumeric){
            onEqual(view)
            operationClicked = true
            operationSign = (view as Button).text.toString()
            val stringInput = tvInput?.text.toString()
            num1 = stringInput.toDouble()
            onEqualClicked = false
        }
        else {
            operationSign = (view as Button).text.toString()
            operationClicked = true
        }
        lastNumeric = false
    }

    fun onEqual(view: View) {
        if(operationSign != ""){
            val stringInput = tvInput?.text.toString()
            val num2 = stringInput.toDouble()

            val result = when (operationSign) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> {
                    if (num2 != 0.0) {
                        num1 / num2
                    } else {
                        0.0
                    }
                }
                else -> {
                    0.0
                }
            }
            operationSign = ""
            num1 = result

            val formattedResult = when {
                result >= 1e9 || result <= -1e9 -> {
                    String.format("%.5e", result)
                }
                result.toLong().toDouble() == result -> {
                    result.toLong().toString()
                }
                else -> {
                    result.toString()
                }
            }

            tvInput?.text = formattedResult
        }
        lastNumeric = false
    }
}