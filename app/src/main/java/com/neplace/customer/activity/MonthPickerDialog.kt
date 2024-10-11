package com.neplace.customer.activity

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatDialog
import com.neplace.customer.R
import java.util.*

class MonthPickerDialog(
    context: Context,
    private val listener: OnMonthYearPickListener
) : AppCompatDialog(context) {

    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker
    private lateinit var btnSelectMonthYear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_month_picker)

        monthPicker = findViewById(R.id.monthPicker)!!
        yearPicker = findViewById(R.id.yearPicker)!!
        btnSelectMonthYear = findViewById(R.id.btnSelectMonthYear)!!

        // Set up the month picker
        monthPicker.minValue = 1
        monthPicker.maxValue = 12

        // Set up the year picker (adjust the range based on your requirements)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = currentYear
        yearPicker.maxValue = currentYear + 10 // Adjust the range as needed

        // Set default selection to current month and year
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // Adding 1 because months are zero-indexed
        monthPicker.value = currentMonth
        yearPicker.value = currentYear

        btnSelectMonthYear.setOnClickListener {
            val selectedMonth = monthPicker.value
            val selectedYear = yearPicker.value
            listener.onMonthYearPick(selectedMonth, selectedYear)
            dismiss()
        }
    }

    interface OnMonthYearPickListener {
        fun onMonthYearPick(month: Int, year: Int)
    }
}
