package com.neplace.customer.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityChangePickUpTimeBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.viewmodel.UpdateRideTimeViewModel
import okhttp3.MultipartBody
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class ChangePickUpTimeActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityChangePickUpTimeBinding
    private val calendar = Calendar.getInstance()
    lateinit var updateRideTimeViewModel: UpdateRideTimeViewModel

    private lateinit var ride_id:String

    lateinit var pickup_time: String
    lateinit var pickup_date: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pick_up_time)
        setOnClick()

        ride_id  = intent.getStringExtra("ride_id").toString()
        pickup_date  = intent.getStringExtra("pickup_date").toString()
        pickup_time  = intent.getStringExtra("pickup_time").toString()
        binding.txtTime.text = pickup_time.toString()
        binding.txtDatePicker.text = pickup_date.toString()
        initViews()

    }

    private fun initViews() {
        updateRideTimeViewModel = ViewModelProvider(this)[UpdateRideTimeViewModel::class.java]

        updateRideTimeViewModel.updateRidesResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if (it.data!!.status) {
                        finish()
                    }
                }

                is BaseResponse.Error -> {
                    Toast.makeText(this, "" + it.msg.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("initViewss", "initViewss: " + it.msg)
                }

                else -> {
                    dismissProgress()
                }
            }
        }

    }


    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.txtContinue.setOnClickListener(this)
        binding.txtDatePicker.setOnClickListener(this)
        binding.txtTime.setOnClickListener(this)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.txtDatePicker -> {
//                showDatePicker()
            }
            R.id.imgBack -> {
                finish()
//                showDatePicker()
            }

            R.id.txtTime -> {
                showTimePicker()
            }

            R.id.txtContinue -> {

                val targetDateTime = convertToDateTime(binding.txtDatePicker.text.toString(), binding.txtTime.text.toString())
                val is24HoursInAdvance = isAtLeast24HoursInAdvance(targetDateTime)

//                if (is24HoursInAdvance) {
                    binding.txtDateError.visibility = View.GONE


                    val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("pickup_date", binding.txtDatePicker.text.toString())
                        .addFormDataPart("pickup_time", binding.txtTime.text.toString())
                        .addFormDataPart("ride_id", ride_id)
                        .build()
                    updateRideTimeViewModel.updateRidesUser(requestBody)


//                } else {
//                    binding.txtDateError.visibility = View.VISIBLE
//                    println("The target date is less than 24 hours in advance.")
//                }
            }


        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTimePicker() {


        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val time = LocalTime.parse(pickup_time, formatter)

        // Extract hours and minutes from the LocalTime object
        val hour = time.hour
        val minute = time.minute
        val second = time.second


        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, second)
            val selectedTime24H = LocalTime.of(selectedHour, selectedMinute)

            val currentTime = LocalTime.now()
            val earliestAllowedTime = LocalTime.of(hour, minute) // 4:00 AM
            val latestAllowedTime = earliestAllowedTime.plusHours(1).plusMinutes(30) // 5:30 AM

            if (selectedTime24H in earliestAllowedTime..latestAllowedTime) {
                binding.txtTime.text = selectedTime
            } else {
                // Show an error or inform the user that only times within the allowed range are allowed
//                Toast.makeText(this, "Please select a time between 4:00 AM and 5:30 AM.", Toast.LENGTH_SHORT).show()

            }
        }, hour, minute, true)

        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isAtLeast24HoursInAdvance(targetDateTime: LocalDateTime): Boolean {
        val currentDateTime = LocalDateTime.now()
        val differenceInHours = ChronoUnit.HOURS.between(currentDateTime, targetDateTime)
        return differenceInHours >= 24
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDateTime(date: String, time: String): LocalDateTime {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        val localDate = LocalDate.parse(date, dateFormatter)
        val localTime = LocalTime.parse(time, timeFormatter)
        return LocalDateTime.of(localDate, localTime)
    }
    private fun showDatePicker() {
        val currentDate = calendar.timeInMillis // Get the current time in milliseconds

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Do something with the selected date
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"   //2023-06-05
                binding.txtDatePicker.text = selectedDate
            }, year, month, day)

        datePickerDialog.datePicker.minDate =
            currentDate // Set the minimum date to the current time
        datePickerDialog.show()
    }

}