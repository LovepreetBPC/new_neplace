package com.example.neplacecustomer.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.neplacecustomer.MapData
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityDriverSelectionBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.GetRideModel
import com.example.neplacecustomer.model.UpdateStatusModel
import com.example.neplacecustomer.viewmodel.CancelRideViewModel
import com.example.neplacecustomer.viewmodel.GetGoogleKeyViewModel
import com.example.neplacecustomer.viewmodel.GetRideDetailViewModel
import com.example.neplacecustomer.viewmodel.NearByDriverViewModel
import com.example.neplacecustomer.viewmodel.RideStatusUpdateViewModel
import com.example.neplacecustomer.viewmodel.SendRatingViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.example.neplacecustomer.common.Constant
import com.nexter.application.retrofit.RetrofitUtils.MAPS_API_KEY
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request

class DriverSelectionActivity : BaseActivity(), View.OnClickListener, OnMapReadyCallback {

    lateinit var binding: ActivityDriverSelectionBinding

    lateinit var nearByDriverViewModel: NearByDriverViewModel
    lateinit var getRideDetailViewModel: GetRideDetailViewModel
    lateinit var rideStatusUpdateViewModel: RideStatusUpdateViewModel
    lateinit var sendRatingViewModel: SendRatingViewModel
    lateinit var googleKeyViewModel: GetGoogleKeyViewModel

    lateinit var pickup_lat: String
    lateinit var pickup_long: String
    lateinit var drop_lat: String
    lateinit var drop_long: String
    lateinit var pickup_location: String
    lateinit var drop_location: String
    lateinit var vehicle_type: String
    lateinit var pickup_time: String
    lateinit var pickup_date: String
    private var driver_phoneNumber: String = ""
    private var driver_id: String = ""
    var driver_name: String = ""

    var driver_image: String = ""
    lateinit var trip_id: String
    var plan_id: String = "1"
    lateinit var user_id: String
    lateinit var senderName: String
    var ride_status: String = ""
    var apiKey: String = "AIzaSyAMAdPcBMgkpCDoycbvxeFh6274KbKCvjQ"
    lateinit var cancelRideViewModel: CancelRideViewModel
    private val TAG = "lovepreet098799"

    private lateinit var mMap: GoogleMap
    private val db = Firebase.firestore
    private lateinit var firestore: FirebaseFirestore
    private lateinit var rideRef: DocumentReference


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var PERMISSION_REQUEST_CODE = 200
    var currentLat = "30.6960754"
    var currentLong = "76.6051696"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_selection)

        pickup_lat = intent.getStringExtra("pickup_lat").toString()
        pickup_long = intent.getStringExtra("pickup_long").toString()
        drop_lat = intent.getStringExtra("drop_lat").toString()
        drop_long = intent.getStringExtra("drop_long").toString()
        pickup_location = intent.getStringExtra("pickup_location").toString()
        drop_location = intent.getStringExtra("drop_location").toString()
        vehicle_type = intent.getStringExtra("vehicle_type").toString()
        trip_id = intent.getStringExtra("id").toString()
        user_id = intent.getStringExtra("user_id").toString()
        ride_status = intent.getStringExtra("ride_status").toString()
        FirebaseApp.initializeApp(this)



        plan_id = sharePref.getString(Constant.PlanID, "").toString()


        initViews()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        getCurrentLocation()

        getDriverInitViews()

        cancelRideInitViews()
        setOnClick()

    }

    override fun onResume() {
        super.onResume()
        setOnClick()
    }

    private fun setOnClick() {
        binding.imgMenu.setOnClickListener(this)
        binding.txtConfirm.setOnClickListener(this)
        binding.relativeGotoFile.setOnClickListener(this)
        binding.imgMessage.setOnClickListener(this)
        binding.imgConfirmedMessage.setOnClickListener(this)
        binding.imgConfirmedPhone.setOnClickListener(this)
        binding.relativeSubmit.setOnClickListener(this)
        binding.relativeContinue.setOnClickListener(this)
        binding.relativeChangePickTime.setOnClickListener(this)
        binding.relativeCancelRequest.setOnClickListener(this)
        binding.relativeCancel.setOnClickListener(this)
        binding.imgDirections.setOnClickListener(this)
        binding.txtCancelRequestOnWay.setOnClickListener(this)
        binding.edtDropLocation.setText(drop_location.toString())
        binding.edtPickupLocation.setText(pickup_location.toString())


//        val tripRef = db.collection("Trip").document(trip_id)
//        tripRef.get().addOnSuccessListener { documentSnapshot ->
//            if (documentSnapshot.exists()) {
//                val trip = documentSnapshot.toObject(UpdateStatusModel::class.java)
//                ride_status = trip?.status.toString()
////                    Toast.makeText(this, "status ->   "+ride_status, Toast.LENGTH_SHORT).show()
//                setRideData()
//                // Do something with the retrieved data
//            } else {
//                // Document does not exist
//            }
//        }.addOnFailureListener { e ->
//            // Error occurred while retrieving data
//        }


        val tripRef = db.collection("Trip").document(trip_id)
        tripRef.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Log.e(TAG, "Listen failed", e)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val trip = documentSnapshot.toObject(UpdateStatusModel::class.java)
                ride_status = trip?.status.toString()
                setRideData()

//                if (ride_status == "accepted") {
//                    getRideDetailViewModel.getRide(trip_id)
//
//                } else {
//                    setRideData()
//
//                }

                Log.d(TAG, "Current ride status: $ride_status")
//                getDriverInitViews()

                // Do something with the retrieved data
            } else {
                Log.d(TAG, "Current data: null")
                ride_status = "completed"
                // Document does not exist
                setRideData()

            }
        }


        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        )


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }else{
            Places.initialize(applicationContext, apiKey.toString())

        }

        // Map Fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
        mapFragment.getMapAsync {
            mMap = it
            val originLocation = LatLng(pickup_lat.toDouble(), pickup_long.toDouble())
            mMap.addMarker(MarkerOptions().position(originLocation))
            val destinationLocation = LatLng(drop_lat.toDouble(), drop_long.toDouble())
            mMap.addMarker(MarkerOptions().position(destinationLocation))
            val urll = getDirectionURL(originLocation, destinationLocation, apiKey?:"")
            GetDirection(urll).execute()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))

        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.txtConfirm -> {
                rideStatusUpdateViewModel.updateRideStatus("confirmed", trip_id, false)
                updateRideStatus("confirmed")
            }

            R.id.relativeGotoFile -> {
                rideStatusUpdateViewModel.updateRideStatus("confirmed", trip_id, false)
                updateRideStatus("confirmed")

            }

            /*R.id.relativeCancel -> {
                rideStatusUpdateViewModel.updateRideStatus("canceled", trip_id, true)
                updateRideStatus("active")


            }*/

            R.id.imgMenu -> {
                finish()
//                startActivity(Intent(this, SideMenuActivity::class.java))

            }

            R.id.imgMessage -> {
                startActivity(
                    Intent(this, ChatActivity::class.java)
                        .putExtra("driver_id", driver_id.toString())
                        .putExtra("trip_id", trip_id.toString())
                        .putExtra("driver_phoneNumber", driver_phoneNumber.toString())
                        .putExtra("driver_name", driver_name.toString())
                        .putExtra("driver_image", driver_image.toString())
                )

            }

            R.id.imgConfirmedMessage -> {
                startActivity(
                    Intent(this, ChatActivity::class.java)
                        .putExtra("driver_id", driver_id.toString())
                        .putExtra("trip_id", trip_id.toString())
                        .putExtra("driver_phoneNumber", driver_phoneNumber.toString())
                        .putExtra("driver_name", driver_name.toString())
                        .putExtra("driver_image", driver_image.toString())
                )

            }

            R.id.imgConfirmedPhone -> {
                if (driver_phoneNumber != null) {
                    makePhoneCall(driver_phoneNumber)
                } else {
                    makePhoneCall("")
                }
            }

            R.id.imgDirections -> {
                startActivity(
                    Intent(this, MapBoxActivity::class.java)
                        .putExtra("dropLat", drop_lat.toString())
                        .putExtra("dropLong", drop_long.toString())
                        .putExtra("pickUpLong", currentLong.toString())
                        .putExtra("pickUpLat", currentLat.toString())
                )
            }

            R.id.relativeContinue -> {
                binding.relativeMainBottom.visibility = View.VISIBLE
                binding.relativeConfirm.visibility = View.GONE
                binding.relativeButton.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.relativeRide.visibility = View.GONE
                binding.linerLocation.visibility = View.GONE

//                startActivity(Intent(this, DriverAcceptanceActivity::class.java))
            }

            R.id.relativeCancelRequest -> {
//                showDialog()

//                if (plan_id == "1") {
//                    startActivity(Intent(this, CancelRideDialogActivity::class.java).putExtra("ride_id", trip_id))
//                } else {

                rideStatusUpdateViewModel.updateRideStatus("canceled", trip_id, false)
                updateRideStatus("canceled")


//                }

            }

            R.id.txtCancelRequestOnWay -> {
                rideStatusUpdateViewModel.updateRideStatus("canceled", trip_id, false)
                updateRideStatus("canceled")
            }


            R.id.relativeChangePickTime -> {
                startActivity(
                    Intent(this, ChangePickUpTimeActivity::class.java)
                        .putExtra("ride_id", trip_id.toString())
                        .putExtra("pickup_date", pickup_date.toString())
                        .putExtra("pickup_time", pickup_time.toString())
                )
            }

            R.id.relativeSubmit -> {

                if (binding.edtReview.text.toString().isEmpty()) {
                    ToastMsg("The description filed is required")
                } else {
                    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("rating", binding.userRating.rating.toInt().toString())
                        .addFormDataPart("rated_user_id", driver_id)
                        .addFormDataPart("ride_id", trip_id)
                        .addFormDataPart("description", binding.edtReview.text.toString())
                        .build()
                    sendRatingViewModel.senReview(requestBody)
                }


            }
        }
    }


    // Method to update ride status in Firestore
    private fun updateRideStatus(newStatus: String) {
        db.collection("Trip").document(trip_id)
            .update("status", newStatus)
            .addOnSuccessListener {
//                Toast.makeText(this, "Ride status updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error updating ride status", e)
//                Toast.makeText(this, "Failed to update ride status", Toast.LENGTH_SHORT).show()
            }


//        val trip = UpdateStatusModel(ride_status)
//        db.collection("Trip").document(trip_id).set(trip).addOnSuccessListener {
//
//            setRideData()
//
//            // Data saved successfully
//        }.addOnFailureListener { e ->
//            // Error occurred while saving data
//        }
    }

    private fun initViews() {

        googleKeyViewModel = ViewModelProvider(this)[GetGoogleKeyViewModel::class.java]
        rideStatusUpdateViewModel = ViewModelProvider(this)[RideStatusUpdateViewModel::class.java]
        sendRatingViewModel = ViewModelProvider(this)[SendRatingViewModel::class.java]


        googleKeyViewModel.getGoogleKey()

        googleKeyViewModel.getGoogleKeyResponse.observe(this) { it ->
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    if (it.data!!.status) {
                        MAPS_API_KEY = it.data.data.google_key
                        apiKey = it.data.data.google_key

                    } else {
                        apiKey = "AIzaSyAMAdPcBMgkpCDoycbvxeFh6274KbKCvjQ"
                    }
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }

                else -> {
                    dismissProgress()
                }
            }





            rideStatusUpdateViewModel.rideResponse.observe(this) {
                when (it) {
                    is BaseResponse.Loading -> {
                        showProgress()
                    }

                    is BaseResponse.Success -> {
                        dismissProgress()

                        if (it.data!!.status) {
                            if (ride_status == null) {
//                            Toast.makeText(this, "Null -> " + it.data.data.status, Toast.LENGTH_SHORT).show()
                                ride_status = "active"

                            }else if(it.data.data.status=="canceled") {
                                Toast.makeText(this, ""+it.data.message, Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            else {
                                setRideData()
//                                Toast.makeText(this, ""+it.data.message, Toast.LENGTH_SHORT).show()


                            }
                        }

                    }

                    is BaseResponse.Error -> {
                        processError(it.msg)
                    }

                    else -> {
                        dismissProgress()
                    }
                }
            }

            sendRatingViewModel.addRidesResponse.observe(this) {
                when (it) {
                    is BaseResponse.Loading -> {
                        showProgress()
                    }

                    is BaseResponse.Success -> {
                        dismissProgress()

                        var intent = Intent(this, ThanksActivity::class.java)
                        startActivity(intent)
                        finish()
//                    Toast.makeText(this, "" + it.data!!.message.toString(), Toast.LENGTH_SHORT)
//                        .show()
                    }

                    is BaseResponse.Error -> {
                        processError(it.msg)
                    }

                    else -> {
                        dismissProgress()
                    }
                }
            }


        }

    }

    private fun setRideData() {
        Log.e(TAG, "setRideData:123456789 " + ride_status)

        if (ride_status != null) {

            if (ride_status.equals("accepted")) {
                binding.linerLocation.visibility = View.VISIBLE
                binding.relativeRide.visibility = View.GONE
                binding.relativeMainBottom.visibility = View.VISIBLE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.imgDirections.visibility = View.VISIBLE
            } else if (ride_status.equals("confirmed")) {
                binding.linerLocation.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.relativeOnWay.visibility = View.GONE
                binding.viewConfirmed.visibility = View.GONE
                binding.relativeConfirmedAddress.visibility = View.GONE
                binding.relativeRide.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.VISIBLE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.imgDirections.visibility = View.VISIBLE
            } else if (ride_status.equals("active")) {
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.relativeRide.visibility = View.GONE
                binding.linerLocation.visibility = View.VISIBLE
                binding.imgDirections.visibility = View.GONE

            } else if (ride_status.equals("Active")) {
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.relativeRide.visibility = View.GONE
                binding.linerLocation.visibility = View.VISIBLE
                binding.imgDirections.visibility = View.GONE

            } else if (ride_status.equals("completed")) {
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.VISIBLE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.relativeRide.visibility = View.VISIBLE
                binding.linerLocation.visibility = View.GONE
                binding.imgDirections.visibility = View.GONE

            } else if (ride_status.equals("end_trip")) {
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.VISIBLE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.relativeRide.visibility = View.VISIBLE
                binding.linerLocation.visibility = View.GONE
                binding.imgDirections.visibility = View.GONE

            } else if (ride_status.equals("start_ride")) {
                binding.relativeMainBottom.visibility = View.VISIBLE
                binding.relativeConfirm.visibility = View.VISIBLE
                binding.linerLocation.visibility = View.GONE
                binding.imgDirections.visibility = View.VISIBLE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeRide.visibility = View.VISIBLE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
            } else if (ride_status.equals("pickup_city")) {
                binding.linerLocation.visibility = View.GONE
                binding.relativeMainBottom.visibility = View.VISIBLE
                binding.relativeRide.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.imgDirections.visibility = View.VISIBLE
            } else if (ride_status.equals("start_trip")) {
                binding.linerLocation.visibility = View.GONE
                binding.relativeStartTip.visibility = View.VISIBLE
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeRide.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.imgDirections.visibility = View.VISIBLE
            } else if (ride_status == "goto_pickup") {
                binding.linerLocation.visibility = View.GONE
                binding.relativeConfirm.visibility = View.VISIBLE
                binding.relativeOnWay.visibility = View.VISIBLE
                binding.viewConfirmed.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeConfirmedAddress.visibility = View.GONE
                binding.relativeRide.visibility = View.GONE
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.imgDirections.visibility = View.VISIBLE
            }
            else if (ride_status == "canceled") {
                binding.linerLocation.visibility = View.GONE
                binding.relativeConfirm.visibility = View.GONE
                binding.relativeOnWay.visibility = View.GONE
                binding.viewConfirmed.visibility = View.GONE
                binding.relativeStartTip.visibility = View.GONE
                binding.relativeConfirmedAddress.visibility = View.GONE
                binding.relativeRide.visibility = View.GONE
                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
                binding.RelativeRatingDriver.visibility = View.GONE
                binding.imgDirections.visibility = View.GONE
                finish()
                Toast.makeText(this, "Your trip has been canceled by driver ", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("ride_statusNew", "setRideData else :   " + ride_status)
        }
    }

    private fun getDriverInitViews() {

        getRideDetailViewModel = ViewModelProvider(this)[GetRideDetailViewModel::class.java]


        getRideDetailViewModel.getRide(trip_id)
        getRideDetailViewModel.getRideResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    processEdit(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
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

    private fun processEdit(data: GetRideModel?) {
//      ToastMsg( " getRide "+"DriverSection :  driver_name  -> $driver_name ,  driver_phoneNumber  -> $driver_phoneNumber   , driver_image -> $driver_image")
//        ride_status = data!!.data.ride_status
//        Log.e("ChatActivity", "DriverSection :  driver_name  -> ${data!!.data.driver_name} ,  driver_phoneNumber  -> ${data.data.driver_phone}  , driver_image -> ${data.data.driver_image} ")


        setRideData()

        try {

            user_id = data!!.data.user_id.toString()
            if (data.data.driver_id.toString().isNotEmpty() && data.data.driver_id != 0) {
                Log.e("driver_id", "processEdit: " + data.data.driver_id.toString())



                Glide.with(this).load(Constant.BASEURL + data.data.driver_image)
                    .error(R.mipmap.img_place_holder).into(binding.imgProfile)
                binding.ratingBar.numStars = 5
                binding.ratingBar.rating = data.data.driver_rating!!.toFloat()
                binding.txtPrice.text = "$" + data.data.price.toString()
                binding.txtratingValue.text = data.data.driver_rating.toString()
                binding.txtVehicleName.text = vehicle_type
                binding.txtName.text = data.data.driver_name
                binding.txtStartAddress.text = pickup_location

                if (data.data.airline != null) {

                    if (data.data.airline != null) {
                        binding.txtAddress.text = data.data.airline + "\n" + data.data.airport_code
                    } else {
                        binding.txtAddress.text = data.data.airline
                    }

                } else {
                    binding.txtAddress.text = data.data.drop_location

                }


                binding.txtTime.text = data.data.miles.toString() + "Miles Away"

                binding.txtMiless.text = data.data.miles.toString() + "Miles"

                binding.txtTimes.text = "2 min"

                pickup_time = data.data.pickup_time.toString()
                pickup_date = data.data.pickup_date.toString()
                //confirm_ride

                Glide.with(this).load(Constant.BASEURL + data.data.driver_image)
                    .error(R.mipmap.img_place_holder).into(binding.imgConfirmedProfile)

                binding.ratingConfirmedBar.numStars = 5
                binding.ratingConfirmedBar.rating = data.data.driver_rating.toFloat()
                binding.txtConfirmedratingValue.text = data.data.driver_rating.toString()
                binding.txtConfirmedVehicleName.text = vehicle_type
                binding.txtConfirmedName.text = data.data.driver_name
                binding.txtStartAddressConfirmed.text = pickup_location
                binding.txtConfirmedTime.text = data.data.miles.toString() + "Miles Away"

                driver_phoneNumber = data.data.driver_phone.toString()
                driver_name = data.data.driver_name.toString()
                driver_id = data.data.driver_id.toString()
                driver_image = data.data.driver_image.toString()


                //rating
                Glide.with(this).load(Constant.BASEURL + data.data.driver_image)
                    .error(R.mipmap.img_place_holder).into(binding.imgDriverProfile)

                binding.txtDriverName.text = data.data.driver_name
                binding.txtRatingValue.setText(data.data.driver_rating)
                binding.ratingTop.rating = data.data.driver_rating.toFloat()


            }
            else {
                Glide.with(this).load(Constant.BASEURL + data.data.driver_image)
                    .error(R.mipmap.img_place_holder).into(binding.imgProfile)

                binding.relativeMainBottom.visibility = View.GONE
                binding.relativeWaitingDriver.visibility = View.GONE
            }


        } catch (e: Exception) {
            Log.e(TAG, "processEdit catch -: " + e.message)
        }

    }


    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.booking_cancel_dialog_layout)
        val noContinue = dialog.findViewById(R.id.relativeNoContinue) as RelativeLayout
        val yesCancel = dialog.findViewById(R.id.relativeYesCancel) as RelativeLayout

        noContinue.setOnClickListener {
            dialog.dismiss()
        }
        yesCancel.setOnClickListener {
            cancelRideViewModel.cancelRide(user_id)
            updateRideStatus("canceled")
            dialog.dismiss()

        }

        dialog.show()

    }

    private fun cancelRideInitViews() {

        cancelRideViewModel = ViewModelProvider(this)[CancelRideViewModel::class.java]


        cancelRideViewModel.cancelRideResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    ToastMsg(it.data!!.message.toString())

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                    ToastMsg(it.msg.toString())
                }

                else -> {
                    dismissProgress()
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val originLocation = LatLng(pickup_lat.toDouble(), pickup_long.toDouble())
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
        val destinationLocation = LatLng(drop_lat.toDouble(), drop_long.toDouble())
        val urll = getDirectionURL(originLocation, destinationLocation, apiKey ?: "")
        GetDirection(urll).execute()
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
//        Log.e("rehal123456", "getDirectionURL: "+ "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret")
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret"


    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
                Log.e("rehal1234567", "doInBackground:Success -> yaaaaaaaa")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("rehal1234567", "doInBackground: " + e.message)
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLACK)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    private fun makePhoneCall(phoneNumber: String) {

        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
//        val callIntent = Intent(Intent.ACTION_DIAL)
//        callIntent.data = Uri.parse("tel:$phoneNumber")
//
//        if (this.packageManager.resolveActivity(callIntent, 0) != null) {
//            startActivity(callIntent)
//        } else {
//            Toast.makeText(this, "No phone app found", Toast.LENGTH_SHORT).show()
//        }
    }


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    currentLat = location.latitude.toString()
                    currentLong = location.longitude.toString()
                    // Once you have the location, you can update the map

//                    Toast.makeText(this, "lat-> $currentLat , long-> $currentLong", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location
                getCurrentLocation()
            } else {
                // Permission denied
                // Handle this according to your app's logic
            }
        }
    }

}