package com.example.neplacecustomer.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityScheduleRideBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.AddRidesModel
import com.example.neplacecustomer.model.FlightNumberData
import com.example.neplacecustomer.viewmodel.AddRidesViewModel
import com.example.neplacecustomer.viewmodel.AirPortCodeViewModel
import com.example.neplacecustomer.viewmodel.AirlineViewModel
import com.example.neplacecustomer.viewmodel.EtaNumberViewModel
import com.example.neplacecustomer.viewmodel.FlightNumberViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class ScheduleRideActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityScheduleRideBinding
    private val calendar = Calendar.getInstance()
    lateinit var viewModel: AddRidesViewModel
    lateinit var airPortCodeModel: AirPortCodeViewModel
    lateinit var airLineViewModel: AirlineViewModel
    lateinit var flightNumberViewModel: FlightNumberViewModel
    lateinit var etaNumberViewModel: EtaNumberViewModel

    private val categoryData = ArrayList<String>()
    private val airlineData = ArrayList<String>()
    private val flightData = ArrayList<FlightNumberData>()
    private val etaData = ArrayList<String>()

    var seatNumber = 6
    var selectPassenger: Int = 0
    var selectLuggage = 0
    var selectChild = 0
    var selectHandicap = 0
    val arrayList = ArrayList<Int>()
    lateinit var dialog: Dialog

    lateinit var pickup_lat: String
    lateinit var pickup_long: String
    lateinit var drop_lat: String
    lateinit var drop_long: String
    lateinit var pickup_location: String
    lateinit var vehicleType: String
    lateinit var pickup_city: String
    lateinit var drop_location: String
    var rideStatus: String = "Active"
    var airPortCodeName = ""
    var airLineName = ""
    var flightName = ""
    var etaName = ""

    var airportCodeArrayList = ArrayList<String>()
    var airLineArrayList = ArrayList<String>()
    var flightArrayList = ArrayList<String>()
    var etaArrayList = ArrayList<String>()


    private val serviceTypeList = arrayOf("Select Service type", "From Airport", "To Airport", "Point-To-Point", "Hourly As-Directed")
    private val hoursList = arrayOf("3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18","19", "20", "21", "22", "23", "24")
    var serviceType = ""
    var selectedHours = ""
    var alacart = ""
    val seatsName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_ride)
        pickup_lat = intent.getStringExtra("pickup_lat").toString()
        pickup_long = intent.getStringExtra("pickup_long").toString()
        drop_lat = intent.getStringExtra("drop_lat").toString()
        drop_long = intent.getStringExtra("drop_long").toString()
        pickup_location = intent.getStringExtra("pickup_location").toString()
        drop_location = intent.getStringExtra("drop_location").toString()
        seatNumber = intent.getStringExtra("seat")!!.toString().toInt()
        vehicleType = intent.getStringExtra("vehicleType").toString()
        pickup_city = intent.getStringExtra("pickup_city").toString()
        alacart = intent.getStringExtra("alacart").toString()

        setOnClick()
    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.txtContinue.setOnClickListener(this)
        binding.txtDatePicker.setOnClickListener(this)
        binding.txtTime.setOnClickListener(this)
        initViewss()
        setPassengerSpinner()
        setServiceTypeSpinner()
        setHoursSpinner()
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        val time = SimpleDateFormat("HH:mm:ss")

        // Get current time
        val currentTime = Calendar.getInstance()

        // Add 24 hours to the current time
        currentTime.add(Calendar.HOUR_OF_DAY, 25)

        // Format the date and time
        val currentDate = sdf.format(currentTime.time)
        val formattedTime = time.format(currentTime.time)

        // Set the formatted date and time to your UI elements
        binding.txtDatePicker.setText(currentDate)
        binding.txtTime.setText(formattedTime)
        binding.txtPickUpAddress.setText(pickup_location)
        binding.txtDropOffAddress.setText(drop_location)



        binding!!.edtAirportCode.setOnClickListener(View.OnClickListener {
            dialog = Dialog(this@ScheduleRideActivity)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()
            val editText: EditText = dialog.findViewById(R.id.edit_text)
            val listView: ListView = dialog.findViewById(R.id.list_view)
            val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
                this@ScheduleRideActivity,
                android.R.layout.simple_list_item_1,
                airportCodeArrayList as List<Any?>
            )

            // set adapter
            listView.setAdapter(adapter)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {}
            })
            listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id -> // when item selected from list
                // set selected item on textView
                binding!!.edtAirportCode.setText(adapter.getItem(position).toString())

//                categoryId = arrayList[position].id.toString()
//                categoryName = arrayList[position].name.toString()
                dialog.dismiss()

            })
        });


    }


    private fun initViewss() {
        viewModel = ViewModelProvider(this)[AddRidesViewModel::class.java]
        airPortCodeModel = ViewModelProvider(this)[AirPortCodeViewModel::class.java]
        airLineViewModel = ViewModelProvider(this)[AirlineViewModel::class.java]
        flightNumberViewModel = ViewModelProvider(this)[FlightNumberViewModel::class.java]
        etaNumberViewModel = ViewModelProvider(this)[EtaNumberViewModel::class.java]

        airPortCodeModel.getAirPortCode()
        airLineViewModel.getAirLine()
        flightNumberViewModel.getFlightNumber(airPortCodeName)


        viewModel.addRidesResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                    Log.e("initViewss", "initViewss: " + it.msg)
                }

                else -> {
                    dismissProgress()
                }
            }
        }

        airPortCodeModel.airPortCodeResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    Log.e("airportCodeAPIresponse", "airportCodeAPIresponse: loading")
                }

                is BaseResponse.Success -> {
                    for (i in it.data!!.data.indices) airportCodeArrayList.add(it.data.data[i].code)
                    airPortCodeName = binding.edtAirportCode.text.toString()
                    Log.e(
                        "airportCodeAPIresponse",
                        "airportCodeAPIresponse: Success" + airPortCodeName
                    )
                    Log.e(
                        "airportCodeAPIresponse",
                        "airportCodeAPIresponse: Success" + airportCodeArrayList
                    )
                }

                is BaseResponse.Error -> {
                    Log.e("airportCodeAPIresponse", "airportCodeAPIresponse: Error")
                }

                null -> {
                    Log.e("airportCodeAPIresponse", "airportCodeAPIresponse: null")
                }

                else -> {}
            }
        }



        airLineViewModel.airlineResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    Log.e("airlineAPIresponse", "airlineAPIresponse: loading")
                }

                is BaseResponse.Success -> {
//                    airport_name=it.data!!.data[0].value
                    for (i in it.data!!.data.indices) airLineArrayList.add(it.data.data[i].airline_name)
                    setAirLineData()
                    Log.e("airlineAPIresponse", "airlineAPIresponse: success " + it.data!!.data)
                }

                is BaseResponse.Error -> {
                    Log.e("airlineAPIresponse", "airlineAPIresponse: error")
                }

                null -> {
                    Log.e("airlineAPIresponse", "airlineAPIresponse: null")
                }

                else -> {}
            }
        }


        flightNumberViewModel.flightNumberResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    Log.e("FlightNumberAPIresponse", "FlightNumberAPIresponse: loading")
                }

                is BaseResponse.Success -> {
                    for (i in it.data!!.data.indices) {
                        flightArrayList.add(it.data.data[i].flight_number.toString())
                        flightData.add(it.data.data[i])
                        setFlightData()

                    }

                    Log.e(
                        "FlightNumberAPIresponse",
                        "FlightNumberAPIresponse: flighDataArrayList$flightData"
                    )

                    Log.e(
                        "FlightNumberAPIresponse",
                        "FlightNumberAPIresponse: Success" + it.data!!.data
                    )
                }

                is BaseResponse.Error -> {
                    Log.e("FlightNumberAPIresponse", "FlightNumberAPIresponse: Error")
                }

                null -> {
                    Log.e("FlightNumberAPIresponse", "FlightNumberAPIresponse: null")
                }

                else -> {}
            }
        }


        etaNumberViewModel.etaNumberResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()

                    if (it.data!!.status) {
//                        binding.edtETA.setText(it.data.data.toString())
//                    for (pos in it.data!!.data.indices) {
//                        etaArrayList = ArrayList<String>()

//                        etaArrayList.addAll()

//                        flightData.add(it.data.data[pos].value)
//                        setFlightData()

                    }

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                    Log.e("initViewss", "initViewss: " + it.msg)
                }

                else -> {
                    dismissProgress()
                }
            }
        }

    }

    private fun setAirPortCode() {

//        Log.e("airportCode", "setAirPortCode: "+ s +"\n")
        binding.edtAirportCode.setOnClickListener(View.OnClickListener {
            dialog = Dialog(this@ScheduleRideActivity)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText: EditText = dialog.findViewById(R.id.edit_text)
            val listView: ListView = dialog.findViewById(R.id.list_view)

            Log.e("setAirPortCode", "setAirPortCode: " + categoryData.toString())
            val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
                this@ScheduleRideActivity,
                android.R.layout.simple_list_item_1,
                airportCodeArrayList as List<Any?>
            )

            // set adapter
            listView.setAdapter(adapter)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {}
            })
            listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id -> // when item selected from list
                // set selected item on textView
                binding.edtAirportCode.setText(adapter.getItem(position).toString())
//                airPortCodeName = airportCodeArrayList[position].toString()
                dialog.dismiss()

            })
        });


    }

    private fun setAirLineData() {

        binding.edtAirline.setOnClickListener(View.OnClickListener {
            dialog = Dialog(this@ScheduleRideActivity)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText: EditText = dialog.findViewById(R.id.edit_text)
            val listView: ListView = dialog.findViewById(R.id.list_view)

            Log.e("setAirLineCode", "setAirLine: " + airlineData.toString())
            val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
                this@ScheduleRideActivity,
                android.R.layout.simple_list_item_1,
                airLineArrayList as List<Any?>
            )

            // set adapter
            listView.setAdapter(adapter)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {}
            })
            listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id -> // when item selected from list
                // set selected item on textView
                binding.edtAirline.setText(adapter.getItem(position).toString())
//                airLineName = airlineData[position].toString()

                dialog.dismiss()
            })
        });


    }

    private fun setFlightData() {

        binding.edtFlightNumber.setOnClickListener(View.OnClickListener {
            dialog = Dialog(this@ScheduleRideActivity)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 800)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val editText: EditText = dialog.findViewById(R.id.edit_text)
            val listView: ListView = dialog.findViewById(R.id.list_view)

            Log.e("setFlightData", "flightData: " + flightData.toString())
            val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
                this@ScheduleRideActivity,
                android.R.layout.simple_list_item_1,
                flightArrayList as List<Any?>
            )

            // set adapter
            listView.setAdapter(adapter)
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable) {

                }
            })
            listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id -> // when item selected from list
                // set selected item on textView
                binding.edtFlightNumber.setText(adapter.getItem(position).toString())
                binding.edtETA.setText(flightData[position].eta)
//                flightName = flightData[position].toString()

//                etaNumberViewModel.getEtaNumber(flightName)
                dialog.dismiss()

            })
        });


    }


    private fun processError(msg: String?) {
        ToastMsg(msg.toString())

    }

    private fun processLogin(data: AddRidesModel?) {
        val id = data!!.data.id.toString()
        showDialog(id)
    }


    private fun validation(): Boolean {

        if (serviceType.isEmpty()) {
            ToastMsg("Please Select Service Type")
            return false
        } else if (serviceType == "Select Type") {
            ToastMsg("Please Select Service Type")
            return false
        }/* else if (binding.edtAirportCode.text!!.isEmpty()) {
            ToastMsg("Please Enter Airport Code")
            return false
        } else if (binding.edtAirline.text!!.isEmpty()) {
            ToastMsg("Please Enter Airline ")
            return false
        } else if (binding.edtETA.text!!.isEmpty()) {
            ToastMsg("Please Enter ETA")
            return false
        }*/


        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }

            R.id.txtContinue -> {

                val targetDateTime = convertToDateTime(
                    binding.txtDatePicker.text.toString(), binding.txtTime.text.toString()
                )
                val is24HoursInAdvance = isAtLeast24HoursInAdvance(targetDateTime)
//                if (is24HoursInAdvance) {
//                    binding.txtDateError.visibility = View.GONE


                Log.e("serviceType", "onItemSelected: ooooooooooooo" + serviceType)

                if (validation()) {

                    if (is24HoursInAdvance) {
                        binding.txtDateError.visibility = View.GONE

                        val intent = Intent(this, ScheduleRideReviewActivity::class.java)
                        intent.putExtra("ride_status", rideStatus)
                        intent.putExtra("pickup_date", binding.txtDatePicker.text.toString())
                        intent.putExtra("pickup_time", binding.txtTime.text.toString())
                        intent.putExtra("pickup_location", pickup_location.toString())
                        intent.putExtra("drop_location", drop_location.toString())
                        intent.putExtra("vehicle_type", vehicleType.toString())
                        intent.putExtra("passenger", selectPassenger.toString())
                        intent.putExtra("luggage", selectLuggage.toString())
                        intent.putExtra("child", selectChild.toString())
                        intent.putExtra("seat", seatNumber.toString())
                        intent.putExtra("airPort_code", binding.edtAirportCode.text.toString())
                        intent.putExtra("airline", binding.edtAirline.text.toString())
                        intent.putExtra("flight_number", binding.edtFlightNumber.text.toString())
                        intent.putExtra("pickup_lat", pickup_lat.toString())
                        intent.putExtra("pickup_long", pickup_long.toString())
                        intent.putExtra("drop_lat", drop_lat.toString())
                        intent.putExtra("drop_long", drop_long.toString())
                        intent.putExtra("pickup_city", pickup_city.toString())
                        intent.putExtra("serviceType", serviceType.toString())
                        intent.putExtra("eta", binding.edtETA.text.toString())
                        intent.putExtra("handicap", selectHandicap.toString())
                        intent.putExtra("min_hours", selectedHours.toString())
                        intent.putExtra("alacart", alacart.toString())
                        startActivity(intent)


                    } else {
                        binding.txtDateError.visibility = View.VISIBLE
                        println("The target date is less than 24 hours in advance.")
                    }
                }

//                showDialog()
            }

            R.id.txtDatePicker -> {
                showDatePicker()
            }

            R.id.txtTime -> {
                showTimePicker()
            }

            R.id.spinnerPassenger -> {
            }
        }
    }

    private fun setPassengerSpinner() {

        //setPassengerSpinner

        for (i in 0..seatNumber) {
            arrayList.add(i)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPassenger.adapter = adapter
        binding.spinnerPassenger.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectPassenger = arrayList[position]
                    seatNumber  = selectPassenger-seatNumber
//                    selectPassenger = seatNumber - arrayList[position]

                    Log.e("luggage", "setPassengerSpinner   $selectPassenger")
//                val selectedFruit = parent?.getItemAtPosition(position) as String
                    // Do something with the selected item (selectedFruit)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do something when nothing is selected
                }
            }


        // setLuggageSpinner

        val arrayList2 = ArrayList<Int>()
        for (i in 0..seatNumber) {
            arrayList2.add(i)
        }

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList2)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLuggage.adapter = adapter1
        binding.spinnerLuggage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
//                    selectPassenger = selectChild - arrayList2[position].toInt()
                    selectLuggage = arrayList2[position].toInt()
                    seatNumber - arrayList[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do something when nothing is selected
                }
            }


        //setChildSpinner

        val arrayList3 = ArrayList<Int>()
        for (i in 0..seatNumber) {
            arrayList3.add(i)
        }
        Log.e("child", "setChildSpinner:   $selectPassenger")

        val adapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList3)
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerChild.adapter = adapter3
        binding.spinnerChild.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
//                selectPassenger = arrayList3[position].toInt()
                selectChild = arrayList3[position].toInt()
                seatNumber - arrayList[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }

        //spinnerHandicap

        val arrayList4 = ArrayList<Int>()
        for (i in 0..seatNumber) {
            arrayList4.add(i)
        }
//        Log.e("luggage", "setChildSpinner:   $selectPassenger")

        val adapter4 = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList3)
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHandicap.adapter = adapter4
        binding.spinnerHandicap.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
//                    selectPassenger = arrayList3[position].toInt()
                    selectHandicap = arrayList3[position].toInt()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do something when nothing is selected
                }
            }

    }

    private fun setServiceTypeSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceTypeList)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.spinnerServiceType.adapter = adapter
        binding.spinnerServiceType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener { override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    val selectedItem = "From Airport" // Replace with your selected item
                    val selectedItemPosition = serviceTypeList.indexOf(selectedItem)

                    serviceType = parent?.getItemAtPosition(position) as String

                when (serviceType) {
                    "From Airport" -> {
                        binding.txtHours.visibility = View.GONE
                        binding.relativeHours.visibility = View.GONE

                        binding.txtAirportCode.visibility = View.VISIBLE
                        binding.edtAirportCode.visibility = View.VISIBLE
                        binding.txtAirline.visibility = View.VISIBLE
                        binding.edtAirline.visibility = View.VISIBLE
                        binding.linerFlightNumber.visibility = View.VISIBLE
                    }
                    "To Airport" -> {
                        binding.txtHours.visibility = View.GONE
                        binding.relativeHours.visibility = View.GONE
                        binding.txtAirportCode.visibility = View.VISIBLE
                        binding.edtAirportCode.visibility = View.VISIBLE
                        binding.txtAirline.visibility = View.VISIBLE
                        binding.edtAirline.visibility = View.VISIBLE
                        binding.linerFlightNumber.visibility = View.VISIBLE

                    }
                    "Point-To-Point" -> {
                        binding.txtHours.visibility = View.GONE
                        binding.relativeHours.visibility = View.GONE

                        binding.txtAirportCode.visibility = View.GONE
                        binding.edtAirportCode.visibility = View.GONE
                        binding.txtAirline.visibility = View.GONE
                        binding.edtAirline.visibility = View.GONE
                        binding.linerFlightNumber.visibility = View.GONE

                    }
                    "Hourly As-Directed" -> {
                        binding.relativeHours.visibility = View.VISIBLE
                        binding.txtHours.visibility = View.VISIBLE

                        binding.txtAirportCode.visibility = View.GONE
                        binding.edtAirportCode.visibility = View.GONE
                        binding.txtAirline.visibility = View.GONE
                        binding.edtAirline.visibility = View.GONE
                        binding.linerFlightNumber.visibility = View.GONE
                    }
                }


//                    if (selectedItemPosition != -1) {
//                        Log.e("serviceType", "onItemSelected yaaaaaaaaaa      :   "+serviceType)
//                        serviceType = parent?.getItemAtPosition(position) as String
//                    } else {
//                        serviceType = ""
//                        Log.e("serviceType", "onItemSelected:  noooooooooooooooo  :      "+serviceType)
//                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do something when nothing is selected
                }
            }
    }

    private fun setHoursSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hoursList)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.spinnerHours.adapter = adapter
        binding.spinnerHours.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

                val selectedItem = "3" // Replace with your selected item
                val selectedItemPosition = hoursList.indexOf(selectedItem)

//                    if (selectedItemPosition != -1) {
//                        Log.e("selectedHours", "onItemSelected yaaaaaaaaaa      :   $selectedHours")
                selectedHours = parent?.getItemAtPosition(position) as String
//                    } else {
//                        selectedHours = ""
//                        Log.e("selectedHours", "onItemSelected:  noooooooooooooooo  :      $selectedHours")
//                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }
    }

    private fun showDialog(id: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.booking_success_dialog_layout)
        val noContinue = dialog.findViewById(R.id.relativeBookAnotherRide) as RelativeLayout
        val yesCancel = dialog.findViewById(R.id.relativeGoBack) as RelativeLayout
        val txtTripId = dialog.findViewById(R.id.txtTripId) as TextView
        txtTripId.text = "Trip Id-# $id"
        noContinue.setOnClickListener {
            startActivity(Intent(this, AllRidesActivity::class.java))
            dialog.dismiss()
        }
        yesCancel.setOnClickListener {
            finish()
            dialog.dismiss()

        }

        dialog.show()

    }

    private fun showDatePicker() {
        val currentDate = calendar.timeInMillis // Get the current time in milliseconds

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Do something with the selected date
                //   val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"   //2023-06-05

                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)
                val selectedDate =
                "$formattedMonth-$formattedDay-$selectedYear" // Example: 2023-09-13

                binding.txtDatePicker.text = selectedDate
            }, year, month, day)

        datePickerDialog.datePicker.minDate =
            currentDate // Set the minimum date to the current time
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // Do something with the selected time
            val selectedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, second)
            binding.txtTime.text = selectedTime
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
        val dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        val localDate = LocalDate.parse(date, dateFormatter)
        val localTime = LocalTime.parse(time, timeFormatter)

        return LocalDateTime.of(localDate, localTime)
    }
}

