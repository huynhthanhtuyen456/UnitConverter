package com.example.unitconverter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var etInputValue: EditText
    private lateinit var spinnerFromUnit: Spinner
    private lateinit var spinnerToUnit: Spinner
    private lateinit var btnConvert: Button
    private lateinit var tvResult: TextView
    private lateinit var tvError: TextView

    // Conversion factors relative to Metre
    private val conversionFactors = mapOf(
        "Metre" to 1.0,
        "Millimetre" to 0.001,
        "Mile" to 1609.34,
        "Foot" to 0.3048
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etInputValue = findViewById(R.id.etInputValue)
        spinnerFromUnit = findViewById(R.id.spinnerFromUnit)
        spinnerToUnit = findViewById(R.id.spinnerToUnit)
        btnConvert = findViewById(R.id.btnConvert)
        tvResult = findViewById(R.id.tvResult)
        tvError = findViewById(R.id.tvError)

        val stringArrayFromResources: Array<String> = resources.getStringArray(R.array.length_units_array)

        // Convert the Array<String> to a List<String>
        val units = stringArrayFromResources.toList()

        // Setup Spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromUnit.adapter = adapter
        spinnerToUnit.adapter = adapter

        btnConvert.setOnClickListener {
            convertUnits()
        }
    }

    private fun convertUnits() {
        val inputValueString = etInputValue.text.toString()
        tvError.text = "" // Clear previous errors
        tvResult.text = R.string.result_prefix.toString() // Clear previous result

        if (inputValueString.isEmpty()) {
            tvError.text = R.string.error_empty_input.toString()
            return
        }

        val inputValue = inputValueString.toDoubleOrNull()
        if (inputValue == null) {
            tvError.text = R.string.error_invalid_input.toString()
            return
        }

        val fromUnit = spinnerFromUnit.selectedItem.toString()
        val toUnit = spinnerToUnit.selectedItem.toString()

        if (fromUnit == toUnit) {
            tvResult.text = "Result: ${formatDecimal(inputValue)} $toUnit"
            return
        }

        // Convert 'fromUnit' to Metres first
        val valueInMetres = inputValue * (conversionFactors[fromUnit] ?: 1.0)

        // Convert from Metres to 'toUnit'
        val result = valueInMetres / (conversionFactors[toUnit] ?: 1.0)

        tvResult.text = "Result: ${formatDecimal(result)} $toUnit"
    }

    private fun formatDecimal(number: Double): String {
        // Format to a reasonable number of decimal places
        val df = DecimalFormat("#.####")
        return df.format(number)
    }
}
