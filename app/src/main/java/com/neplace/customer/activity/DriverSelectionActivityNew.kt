package com.neplace.customer.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.neplace.customer.MapData
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityDriverSelectionBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.GetRideModel
import com.neplace.customer.viewmodel.CancelRideViewModel
import com.neplace.customer.viewmodel.GetGoogleKeyViewModel
import com.neplace.customer.viewmodel.GetRideDetailViewModel
import com.neplace.customer.viewmodel.NearByDriverViewModel
import com.neplace.customer.viewmodel.RideStatusUpdateViewModel
import com.neplace.customer.viewmodel.SendRatingViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.neplace.customer.common.Constant
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request





















//
//
//
//class DriverSelectionActivityNew : BaseActivity(), View.OnClickListener, OnMapReadyCallback {
//
//    lateinit var binding: ActivityDriverSelectionBinding
//
//    lateinit var nearByDriverViewModel: NearByDriverViewModel
//    lateinit var getRideDetailViewModel: GetRideDetailViewModel
//    lateinit var rideStatusUpdateViewModel: RideStatusUpdateViewModel
//    lateinit var sendRatingViewModel: SendRatingViewModel
//    lateinit var googleKeyViewModel: GetGoogleKeyViewModel
//
//    lateinit var pickup_lat: String
//    lateinit var pickup_long: String
//    lateinit var drop_lat: String
//    lateinit var drop_long: String
//    lateinit var pickup_location: String
//    lateinit var drop_location: String
//    lateinit var vehicle_type: String
//    private var driver_phoneNumber: String = ""
//    var driver_name: String = ""
//    var driver_id: String = ""
//    var driver_image: String = ""
//    lateinit var trip_id: String
//    lateinit var user_id: String
//    lateinit var senderName: String
//    var ride_status: String = ""
//    var apiKey: String = "AIzaSyAMAdPcBMgkpCDoycbvxeFh6274KbKCvjQ"
//    lateinit var cancelRideViewModel: CancelRideViewModel
//
//    private lateinit var mMap: GoogleMap
//    val db = Firebase.firestore
//    private lateinit var database: FirebaseDatabase
//    private lateinit var tripRef: DatabaseReference
//    private lateinit var tripId: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_selection)
//
//        pickup_lat = intent.getStringExtra("pickup_lat").toString()
//        pickup_long = intent.getStringExtra("pickup_long").toString()
//        drop_lat = intent.getStringExtra("drop_lat").toString()
//        drop_long = intent.getStringExtra("drop_long").toString()
//        pickup_location = intent.getStringExtra("pickup_location").toString()
//        drop_location = intent.getStringExtra("drop_location").toString()
//        vehicle_type = intent.getStringExtra("vehicle_type").toString()
//        trip_id = intent.getStringExtra("id").toString()
//        user_id = intent.getStringExtra("user_id").toString()
//        ride_status = intent.getStringExtra("ride_status").toString()
//
//        getDriverInitViews()
//        initViews()
//        cancelRideInitViews()
//        setOnClick()
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        setOnClick()
//    }
//
//    private fun setOnClick() {
//        binding.imgMenu.setOnClickListener(this)
//        binding.txtConfirm.setOnClickListener(this)
//        binding.relativeGotoFile.setOnClickListener(this)
//        binding.imgMessage.setOnClickListener(this)
//        binding.imgConfirmedMessage.setOnClickListener(this)
//        binding.imgConfirmedPhone.setOnClickListener(this)
//        binding.relativeSubmit.setOnClickListener(this)
//        binding.relativeContinue.setOnClickListener(this)
//        binding.relativeChangePickTime.setOnClickListener(this)
//        binding.relativeCancelRequest.setOnClickListener(this)
//        binding.edtDropLocation.setText(drop_location.toString())
//        binding.edtPickupLocation.setText(pickup_location.toString())
//
//
//        // Initialize Firebase
//        database = FirebaseDatabase.getInstance()
//        tripId = intent.getStringExtra("id").toString()
//        tripRef = database.getReference("Trip").child(tripId)
//
//        // Listen for changes in ride status
//        tripRef.child("status").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val rideStatus = dataSnapshot.getValue(String::class.java)
//                rideStatus?.let {
//                    Log.e("Firebase", "Ride status changed to: $it")
//                    // Update UI or perform actions based on ride status
//                    // For example:
//                    updateUI(rideStatus)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("Firebase", "Failed to read ride status.", databaseError.toException())
//            }
//        })
//
//
//
//        if (!Places.isInitialized()) {
//            Places.initialize(applicationContext, apiKey)
//        }
//
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync {
//            mMap = it
//            val originLocation = LatLng(pickup_lat.toDouble(), pickup_long.toDouble())
//            mMap.addMarker(MarkerOptions().position(originLocation))
//            val destinationLocation = LatLng(drop_lat.toDouble(), drop_long.toDouble())
//            mMap.addMarker(MarkerOptions().position(destinationLocation))
//            val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
//            GetDirection(urll).execute()
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
//
//        }
//    }
//
//    private fun updateRideStatus(status: String) {
//        tripRef.child("status").setValue(status)
//            .addOnSuccessListener {
//                Log.d("Firebase", "Ride status updated successfully to: $status")
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firebase", "Error updating ride status: $status", e)
//            }
//    }
//
//    private fun updateUI(rideStatus: String) {
//        Log.e("Firebase", "rideStatus ----------------->: $rideStatus")
//
//        Toast.makeText(this, "Ride Status -> $rideStatus", Toast.LENGTH_SHORT).show()
//        if (rideStatus != null) {
//
//            if (rideStatus.equals("accepted")) {
//                binding.linerLocation.visibility = View.VISIBLE
//                binding.relativeRide.visibility = View.GONE
//                binding.relativeMainBottom.visibility = View.VISIBLE
//                binding.relativeWaitingDriver.visibility = View.GONE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.GONE
//                binding.relativeConfirm.visibility = View.GONE
//            } else if (rideStatus.equals("confirmed")) {
//                binding.linerLocation.visibility = View.VISIBLE
//                binding.relativeConfirm.visibility = View.VISIBLE
//                binding.relativeOnWay.visibility = View.GONE
//                binding.viewConfirmed.visibility = View.VISIBLE
//                binding.relativeConfirmedAddress.visibility = View.VISIBLE
//                binding.relativeRide.visibility = View.GONE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.relativeMainBottom.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.GONE
//            } else if (rideStatus.equals("active")) {
//                binding.relativeMainBottom.visibility = View.GONE
//                binding.relativeConfirm.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.GONE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.VISIBLE
//                binding.relativeRide.visibility = View.GONE
//                binding.linerLocation.visibility = View.GONE
//
//            } else if (rideStatus.equals("completed")) {
//                binding.relativeMainBottom.visibility = View.GONE
//                binding.relativeConfirm.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.VISIBLE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//                binding.relativeRide.visibility = View.VISIBLE
//                binding.linerLocation.visibility = View.GONE
//
//            } else if (rideStatus.equals("end_trip")) {
//                binding.relativeMainBottom.visibility = View.GONE
//                binding.relativeConfirm.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.VISIBLE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//                binding.relativeRide.visibility = View.VISIBLE
//                binding.linerLocation.visibility = View.GONE
//
//            } else if (rideStatus.equals("start_ride")) {
//                binding.relativeMainBottom.visibility = View.VISIBLE
//                binding.relativeConfirm.visibility = View.VISIBLE
//                binding.linerLocation.visibility = View.VISIBLE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.relativeRide.visibility = View.VISIBLE
//                binding.RelativeRatingDriver.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//            } else if (rideStatus.equals("pickup_city")) {
//                binding.linerLocation.visibility = View.VISIBLE
//                binding.relativeMainBottom.visibility = View.VISIBLE
//                binding.relativeRide.visibility = View.GONE
//                binding.relativeConfirm.visibility = View.GONE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//            } else if (rideStatus.equals("start_trip")) {
//                binding.linerLocation.visibility = View.GONE
//                binding.relativeStartTip.visibility = View.VISIBLE
//                binding.relativeMainBottom.visibility = View.GONE
//                binding.relativeRide.visibility = View.GONE
//                binding.relativeConfirm.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//            } else if (rideStatus.equals("goto_pickup")) {
//                binding.linerLocation.visibility = View.VISIBLE
//                binding.relativeConfirm.visibility = View.VISIBLE
//                binding.relativeOnWay.visibility = View.VISIBLE
//                binding.viewConfirmed.visibility = View.GONE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.relativeConfirmedAddress.visibility = View.GONE
//                binding.relativeRide.visibility = View.GONE
//                binding.relativeMainBottom.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.GONE
//            }
//        } else {
//            Log.e("rideStatusNew", "setRideData else :   $rideStatus")
//        }
//
//    }
//
//    override fun onClick(p0: View?) {
//        when (p0!!.id) {
//            R.id.txtConfirm -> {
//                rideStatusUpdateViewModel.updateRideStatus("confirmed", trip_id,false)
//                updateRideStatus("confirmed")
//            }
//
//            R.id.relativeGotoFile -> {
//                rideStatusUpdateViewModel.updateRideStatus("go_to_pickup", trip_id,false)
//                updateRideStatus("confirmed")
//
//            }
//
//            R.id.imgMenu -> {
//                finish()
//
//            }
//
//            R.id.imgMessage -> {
//                startActivity(
//                    Intent(this, ChatActivity::class.java)
//                        .putExtra("driver_id", driver_id.toString())
//                        .putExtra("driver_phoneNumber", driver_phoneNumber.toString())
//                        .putExtra("driver_name", driver_name.toString())
//                        .putExtra("driver_image", driver_image.toString())
//                )
//
//            }
//
//            R.id.imgConfirmedMessage -> {
//                startActivity(
//                    Intent(this, ChatActivity::class.java)
//                        .putExtra("driver_id", driver_id.toString())
//                        .putExtra("driver_phoneNumber", driver_phoneNumber.toString())
//                        .putExtra("driver_name", driver_name.toString())
//                        .putExtra("driver_image", driver_image.toString())
//                )
//
//            }
//
//            R.id.imgConfirmedPhone -> {
//                if (driver_phoneNumber != null) {
//                    makePhoneCall(driver_phoneNumber)
//                } else {
//                    makePhoneCall("")
//                }
//            }
//
//            R.id.relativeContinue -> {
//                binding.relativeMainBottom.visibility = View.VISIBLE
//                binding.relativeConfirm.visibility = View.GONE
//                binding.relativeButton.visibility = View.GONE
//                binding.RelativeRatingDriver.visibility = View.GONE
//                binding.relativeStartTip.visibility = View.GONE
//                binding.relativeWaitingDriver.visibility = View.GONE
//                binding.relativeRide.visibility = View.GONE
//                binding.linerLocation.visibility = View.GONE
//            }
//
//            R.id.relativeCancelRequest -> {
//                showDialog()
//            }
//
//            R.id.relativeChangePickTime -> {
//                startActivity(
//                    Intent(this, ChangePickUpTimeActivity::class.java).putExtra(
//                        "ride_id",
//                        trip_id.toString()
//                    )
//                )
//            }
//
//            R.id.relativeSubmit -> {
//
//                if (binding.edtReview.text.toString().isEmpty()) {
//                    ToastMsg("The description filed is required")
//                } else {
//                    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//                        .addFormDataPart("rating", binding.userRating.rating.toInt().toString())
//                        .addFormDataPart("rated_user_id", user_id)
//                        .addFormDataPart("ride_id", trip_id)
//                        .addFormDataPart("description", binding.edtReview.text.toString())
//                        .build()
//                    sendRatingViewModel.senReview(requestBody)
//                }
//
//
//            }
//        }
//    }
//
//    private fun initViews() {
//        rideStatusUpdateViewModel = ViewModelProvider(this)[RideStatusUpdateViewModel::class.java]
//        sendRatingViewModel = ViewModelProvider(this)[SendRatingViewModel::class.java]
//        googleKeyViewModel = ViewModelProvider(this)[GetGoogleKeyViewModel::class.java]
//
//
//        rideStatusUpdateViewModel.rideResponse.observe(this) {
//            when (it) {
//                is BaseResponse.Loading -> {
//                    showProgress()
//                }
//
//                is BaseResponse.Success -> {
//                    dismissProgress()
//                    getRideDetailViewModel.getRide(trip_id)
//
//                    if (it.data!!.status) {
//
//
//                        if (ride_status == null) {
////                            Toast.makeText(this, "Null -> " + it.data.data.ri, Toast.LENGTH_SHORT).show()
//                            ride_status = it.data.data.status
////                            setRideData()
//                            updateRideStatus(it.data.status.toString())
//
//                        } else {
////                            updateRideStatus(it.data.status.toString())
//
//                        }
//                    }
//
//                }
//
//                is BaseResponse.Error -> {
//                    processError(it.msg)
//                }
//
//                else -> {
//                    dismissProgress()
//                }
//            }
//        }
//
//        sendRatingViewModel.addRidesResponse.observe(this) {
//            when (it) {
//                is BaseResponse.Loading -> {
//                    showProgress()
//                }
//
//                is BaseResponse.Success -> {
//                    dismissProgress()
//
//                    val intent = Intent(this, ThanksActivity::class.java)
//                    startActivity(intent)
//                    finish()
//
//                }
//
//                is BaseResponse.Error -> {
//                    processError(it.msg)
//                }
//
//                else -> {
//                    dismissProgress()
//                }
//            }
//        }
//
//        googleKeyViewModel.getGoogleKeyResponse.observe(this) {
//            when (it) {
//                is BaseResponse.Loading -> {
//                    showProgress()
//                }
//
//                is BaseResponse.Success -> {
//                    dismissProgress()
//                    if (it.data!!.status) {
//                        apiKey = it.data.data.google_key
//
//                    } else {
//                        apiKey = "AIzaSyAMAdPcBMgkpCDoycbvxeFh6274KbKCvjQ"
//                    }
//                }
//
//                is BaseResponse.Error -> {
//                    processError(it.msg)
//                }
//
//                else -> {
//                    dismissProgress()
//                }
//            }
//        }
//
//    }
//
//    private fun getDriverInitViews() {
//
//        getRideDetailViewModel = ViewModelProvider(this)[GetRideDetailViewModel::class.java]
//
//        getRideDetailViewModel.getRide(trip_id)
//        getRideDetailViewModel.getRideResponse.observe(this) {
//            when (it) {
//                is BaseResponse.Loading -> {
//                    showProgress()
//                }
//
//                is BaseResponse.Success -> {
//                    dismissProgress()
//                    processEdit(it.data)
//                }
//
//                is BaseResponse.Error -> {
//                    processError(it.msg)
//                }
//
//                else -> {
//                    dismissProgress()
//                }
//            }
//        }
//
//    }
//
//    private fun processError(msg: String?) {
//        ToastMsg(msg.toString())
//    }
//
//    private fun processEdit(data: GetRideModel?) {
//
//        user_id = data!!.data.user_id.toString()
//        if (data!!.data.driver_id.toString().isNotEmpty() && data.data.driver_id != 0) {
//            Log.e("driver_id", "processEdit: " + data.data.driver_id.toString())
//
//            Glide.with(this).load(Constant.BASEURL + data.data.driver_image).error(R.mipmap.img_place_holder).into(binding.imgProfile)
//            binding.ratingBar.numStars = 5
//            binding.ratingBar.rating = data.data.driver_rating!!.toFloat()
//            binding.txtPrice.setText("$" + data.data.price.toString())
//            binding.txtratingValue.setText(data.data.driver_rating.toString())
//            binding.txtVehicleName.setText(vehicle_type)
//            binding.txtName.setText(data.data.driver_name)
//            binding.txtStartAddress.setText(pickup_location)
//            binding.txtTime.setText(data.data.miles.toString() + "KM Away")
//
//
//            //confirm_ride
//
//            Glide.with(this).load(Constant.BASEURL + data.data.driver_image).error(R.mipmap.img_place_holder).into(binding.imgConfirmedProfile)
//
//            binding.ratingConfirmedBar.numStars = 5
//            binding.ratingConfirmedBar.rating = data.data.driver_rating.toFloat()
//            binding.txtConfirmedratingValue.setText(data.data.driver_rating.toString())
//            binding.txtConfirmedVehicleName.setText(vehicle_type)
//            binding.txtConfirmedName.setText(data.data.driver_name)
//            binding.txtStartAddressConfirmed.setText(pickup_location)
//            binding.txtConfirmedTime.setText(data.data.miles.toString() + "KM Away")
//
//            driver_phoneNumber = data.data.driver_phone.toString()
//            driver_name = data.data.driver_name.toString()
//            driver_id = data.data.driver_id.toString()
//            driver_image = data.data.driver_image.toString()
//
//
//            //rating
//            Glide.with(this).load(Constant.BASEURL + data.data.driver_image).error(R.mipmap.img_place_holder).into(binding.imgDriverProfile)
//
//            binding.txtDriverName.setText(data.data.driver_name)
//            binding.txtRatingValue.setText(data.data.driver_rating)
//            binding.ratingTop.setRating(data.data.driver_rating.toFloat())
//
//
//        } else {
//            binding.relativeMainBottom.visibility = View.GONE
//            binding.relativeWaitingDriver.visibility = View.GONE
//        }
//
//
//    }
//
//
//    private fun showDialog() {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setContentView(R.layout.booking_cancel_dialog_layout)
//        val noContinue = dialog.findViewById(R.id.relativeNoContinue) as RelativeLayout
//        val yesCancel = dialog.findViewById(R.id.relativeYesCancel) as RelativeLayout
//
//        noContinue.setOnClickListener {
//            cancelRideViewModel.cancelRide(user_id)
//            updateRideStatus("canceled")
//            dialog.dismiss()
//        }
//        yesCancel.setOnClickListener {
//            finish()
//            dialog.dismiss()
//
//        }
//
//        dialog.show()
//
//    }
//
//    private fun cancelRideInitViews() {
//
//        cancelRideViewModel = ViewModelProvider(this)[CancelRideViewModel::class.java]
//        cancelRideViewModel.cancelRideResponse.observe(this) {
//            when (it) {
//                is BaseResponse.Loading -> {
//                    showProgress()
//                }
//
//                is BaseResponse.Success -> {
//                    dismissProgress()
//                    ToastMsg(it.data!!.message.toString())
//
//                }
//
//                is BaseResponse.Error -> {
//                    processError(it.msg)
//                    ToastMsg(it.msg.toString())
//                }
//
//                else -> {
//                    dismissProgress()
//                }
//            }
//        }
//    }
//
//    override fun onMapReady(p0: GoogleMap) {
//        mMap = p0!!
//        val originLocation = LatLng(pickup_lat.toDouble(), pickup_long.toDouble())
//        mMap.clear()
//        mMap.addMarker(MarkerOptions().position(originLocation))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
//        val destinationLocation = LatLng(drop_lat.toDouble(), drop_long.toDouble())
//        val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
//        GetDirection(urll).execute()
//    }
//
//    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
////        Log.e("rehal123456", "getDirectionURL: "+ "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret")
//        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret"
//
//
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private inner class GetDirection(val url: String) :
//        AsyncTask<Void, Void, List<List<LatLng>>>() {
//        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            val data = response.body!!.string()
//
//            val result = ArrayList<List<LatLng>>()
//            try {
//                val respObj = Gson().fromJson(data, MapData::class.java)
//                val path = ArrayList<LatLng>()
//                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
//                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
//                }
//                result.add(path)
//                Log.e("rehal1234567", "doInBackground:Success -> yaaaaaaaa")
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.e("rehal1234567", "doInBackground: " + e.message)
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: List<List<LatLng>>) {
//            val lineoption = PolylineOptions()
//            for (i in result.indices) {
//                lineoption.addAll(result[i])
//                lineoption.width(10f)
//                lineoption.color(Color.BLACK)
//                lineoption.geodesic(true)
//            }
//            mMap.addPolyline(lineoption)
//        }
//    }
//
//    fun decodePolyline(encoded: String): List<LatLng> {
//        val poly = ArrayList<LatLng>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//        while (index < len) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lat += dlat
//            shift = 0
//            result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lng += dlng
//            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
//            poly.add(latLng)
//        }
//        return poly
//    }
//
//    private fun makePhoneCall(phoneNumber: String) {
//
//        val uri = Uri.parse("tel:$phoneNumber")
//        val intent = Intent(Intent.ACTION_DIAL, uri)
//        startActivity(intent)
//
//    }
//}