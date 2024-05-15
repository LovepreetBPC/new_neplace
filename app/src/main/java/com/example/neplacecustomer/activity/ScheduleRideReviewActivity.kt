package com.example.neplacecustomer.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityScheduleRideReviewBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.AddRidesModel
import com.example.neplacecustomer.model.UpdateStatusModel
import com.example.neplacecustomer.viewmodel.AddRidesViewModel
import com.example.neplacecustomer.viewmodel.GetProfileViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

class ScheduleRideReviewActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityScheduleRideReviewBinding
    lateinit var viewModel: AddRidesViewModel


    lateinit var rideStatus: String
    lateinit var pickup_date: String
    lateinit var pickup_time: String
    lateinit var pickup_location: String
    lateinit var drop_location: String
    lateinit var passenger: String
    lateinit var luggage: String
    lateinit var child: String
    lateinit var seat: String
    lateinit var airPort_code: String
    lateinit var airline: String
    lateinit var flight_number: String
    lateinit var pickup_lat: String
    lateinit var pickup_long: String
    lateinit var drop_lat: String
    lateinit var drop_long: String
    var payment_type: String = "fixed"
    lateinit var vehicleType: String
    lateinit var pickup_city: String
    lateinit var serviceType: String
    lateinit var handicap: String
    lateinit var min_hours: String
    lateinit var alacart: String
    lateinit var eta: String
    var seatNumber: Int = 0
    private val db = Firebase.firestore
    lateinit var getProfileViewModel: GetProfileViewModel
    var subscribePlan:Boolean= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_ride_review)
        FirebaseApp.initializeApp(this)

        rideStatus = intent.getStringExtra("rideStatus").toString()
        pickup_date = intent.getStringExtra("pickup_date").toString()
        pickup_time = intent.getStringExtra("pickup_time").toString()
        pickup_location = intent.getStringExtra("pickup_location").toString()
        drop_location = intent.getStringExtra("drop_location").toString()
        vehicleType = intent.getStringExtra("vehicle_type").toString()
        passenger = intent.getStringExtra("passenger").toString()
        luggage = intent.getStringExtra("luggage").toString()
        child = intent.getStringExtra("child").toString()
        seat = intent.getStringExtra("seat").toString()
        airPort_code = intent.getStringExtra("airPort_code").toString()
        airline = intent.getStringExtra("airline").toString()
        flight_number = intent.getStringExtra("flight_number").toString()
        pickup_lat = intent.getStringExtra("pickup_lat").toString()
        pickup_long = intent.getStringExtra("pickup_long").toString()
        drop_lat = intent.getStringExtra("drop_lat").toString()
        drop_long = intent.getStringExtra("drop_long").toString()
        pickup_city = intent.getStringExtra("pickup_city").toString()
        serviceType = intent.getStringExtra("serviceType").toString()
        eta = intent.getStringExtra("eta").toString()
        handicap = intent.getStringExtra("handicap").toString()
        min_hours = intent.getStringExtra("min_hours").toString()
        alacart = intent.getStringExtra("alacart").toString()


        Log.e("pickup_date", "onCreate: $pickup_time    ,   $pickup_date")

        if (serviceType == "From Airport") {
            binding.linearAiportCodespinnerServiceType.visibility = View.VISIBLE
            binding.linearFlightNo.visibility = View.VISIBLE
        } else if (serviceType == "To Airport") {
            binding.linearAiportCodespinnerServiceType.visibility = View.VISIBLE
            binding.linearFlightNo.visibility = View.VISIBLE
        } else if (serviceType == "Point-To-Point") {
            binding.linearAiportCodespinnerServiceType.visibility = View.GONE
            binding.linearFlightNo.visibility = View.GONE
        } else if (serviceType == "Hourly As-Directed") {
            payment_type = "hourly"
            binding.linearAiportCodespinnerServiceType.visibility = View.GONE
            binding.linearFlightNo.visibility = View.GONE
        }


        binding.txtPickUpDate.setText(pickup_date)
        binding.txtPickUpTime.setText(pickup_time)
        binding.txtPickUpAddress.setText(pickup_location)
        binding.txtDropOffAddress.setText(drop_location)
        binding.txtVehicle.setText(vehicleType)
        binding.txtPassengers.setText(passenger)
        binding.txtNumberofLuggage.setText(luggage)
        binding.txtChildSeat.setText(child)
        binding.txtAirportCode.setText(airPort_code)
        binding.txtAirline.setText(airline)
        binding.txtFlightNo.setText(flight_number)
        binding.txtETA.setText(eta)
        binding.txtHandicapSeat.setText(handicap)



        binding.imgBack.setOnClickListener(this)
        binding.relativeRest.setOnClickListener(this)
        binding.txtContinue.setOnClickListener(this)


        initViewss()


    }


    private fun initViewss() {
        viewModel = ViewModelProvider(this)[AddRidesViewModel::class.java]
        getProfileViewModel = ViewModelProvider(this)[GetProfileViewModel::class.java]


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

        getProfileViewModel.getProfileUser()

        getProfileViewModel.profileEditResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if (it.data!!.status) {
                        subscribePlan = it.data.data.subscription
                        if (it.data.data.subscription) {
                            if (serviceType == "Hourly As-Directed") {
                                payment_type = "hourly"
                            } else {
                                payment_type = "fixed"
                            }

                        } else {
//                            payment_type = ""
                            if (serviceType == "Hourly As-Directed") {
                                payment_type = "hourly"
                            } else {
                                payment_type = "fixed"
                            }

                        }

                    } else {
                        Log.e("getProfileError", "initViewss: " + it.data.message.toString())
                    }
                }

                is BaseResponse.Error -> {
                    Toast.makeText(this, "" + it.msg, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    dismissProgress()
                }
            }
        }


    }


    private fun processError(msg: String?) {
        ToastMsg(msg.toString())

    }

    private fun processLogin(data: AddRidesModel?) {
//        ToastMsg(data!!.message.toString())
        val id = data!!.data.id.toString()


        val trip = UpdateStatusModel("active")
        db.collection("Trip").document(id).set(trip).addOnSuccessListener {
//            Toast.makeText(this, "Success  -> $id", Toast.LENGTH_SHORT).show()
            Log.e("upload ", "processLogin: sucess ")

            // Data saved successfully
        }.addOnFailureListener { e ->
            // Error occurred while saving data
//            Toast.makeText(this, "Error  -> $id", Toast.LENGTH_SHORT).show()
        }

        showDialog(id)

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
            startActivity(Intent(this, BookRideActivity::class.java).putExtra("subscribePlan",subscribePlan))
            finish()
            dialog.dismiss()
        }
        yesCancel.setOnClickListener {

            val intent = Intent(this, AllRidesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            dialog.dismiss()

        }

        dialog.show()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.imgBack -> {
                finish()
            }

            R.id.relativeRest -> {
                finish()
            }


            R.id.txtContinue -> {

                val targetDateTime = convertToDateTime(
                    binding.txtPickUpDate.text.toString(), binding.txtPickUpTime.text.toString()
                )
                val is24HoursInAdvance = isAtLeast24HoursInAdvance(targetDateTime)
                if (is24HoursInAdvance) {
                    val inputFormat = SimpleDateFormat("MM-dd-yyyy")
                    val outputFormat = SimpleDateFormat("yyyy-MM-dd")

                    val date: Date? = inputFormat.parse(pickup_date)
                    val pickupDate = outputFormat.format(date!!)

                    val requestBody = MultipartBody.Builder()

                        .setType(MultipartBody.FORM)
                        .addFormDataPart("ride_status", "Active")
                        .addFormDataPart("pickup_lat", pickup_lat)
                        .addFormDataPart("pickup_long", pickup_long)
                        .addFormDataPart("drop_lat", drop_lat)
                        .addFormDataPart("drop_long", drop_long)
                        .addFormDataPart("type", payment_type)
                        .addFormDataPart("pickup_location", pickup_location)
                        .addFormDataPart("drop_location", drop_location)
                        .addFormDataPart("pickup_date", pickupDate.toString())
                        .addFormDataPart("pickup_time", pickup_time)
                        .addFormDataPart("flight_number", flight_number)
                        .addFormDataPart("airline", airline)
                        .addFormDataPart("airport_code", airPort_code)
                        .addFormDataPart("service_type", serviceType)
                        .addFormDataPart("no_of_passemger", seatNumber.toString())
                        .addFormDataPart("handicap", handicap)
                        .addFormDataPart("luggage", luggage.toString())
                        .addFormDataPart("child_seat", child.toString())
                        .addFormDataPart("vehicle_type", vehicleType)
                        .addFormDataPart("pickup_city", pickup_city)
                        .addFormDataPart("min_hours", min_hours)
//                        .addFormDataPart("alacart", alacart)
                        .build()

                    Log.e(
                        "requestBody", "rideStatus :  $rideStatus , " +
                                "  pickup_lat :  $pickup_lat ," +
                                "  pickup_long :  $pickup_long ," +
                                "  drop_lat :  $drop_lat ," +
                                "  drop_long :  $drop_long ," +
                                "  pickup_location :  $pickup_location ," +
                                "  drop_location :  $drop_location ," +
                                "  pickup_date :  ${binding.txtDatePicker.text.toString()}" +
                                "  pickup_time :  ${binding.txtTime.text.toString()}" +
                                "  flight_number :  $flight_number" +
                                "  airline :  $airline" +
                                "  airport_code :  $airPort_code" +
                                "  service_type :  $serviceType" +
                                "  seatNumber :  $seatNumber" +
                                "  luggage :  $luggage" +
                                "  child :  $child" +
                                "  vehicleType :  $vehicleType" +
                                "  pickup_city :  $pickup_city"
                    )
                    viewModel.getRidesUser(requestBody)
                } else {
                    Toast.makeText(
                        this,
                        "Please select pick up time after 24 hrs",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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