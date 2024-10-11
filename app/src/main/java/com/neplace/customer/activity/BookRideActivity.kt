package com.neplace.customer.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.neplace.customer.MapData
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityBookRideBinding
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.utils.Utils
import com.neplace.customer.viewmodel.GetGoogleKeyViewModel
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.neplace.customer.retrofit.RetrofitUtils.MAPS_API_KEY
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.Locale

class BookRideActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    lateinit var binding: ActivityBookRideBinding
    private var REQUEST_AUTOCOMPLETE = 201
    private var PERMISSION_REQUEST_CODE = 200
    private var state: String = ""
    private var pickup_city: String = ""
    private var country: String = ""
    private lateinit var pickup_lat: String
    private lateinit var pickup_lon: String
    private lateinit var drop_lat: String
    private lateinit var drop_long: String


    private lateinit var mMap: GoogleMap
    private var originLatitude: Double = 0.0
    private var originLongitude: Double = 0.0
    private var destinationLatitude: Double = 0.0
    private var destinationLongitude: Double = 0.0
    var subscribePlan = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var googleKeyViewModel : GetGoogleKeyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_ride)

        Log.e("12345678", "book ride: $MAPS_API_KEY")
        val apiKeys = Utils.getApiKeys(this)

        MAPS_API_KEY = apiKeys["MAPS_API_KEY"] ?: getString(R.string.mapbox_access_token)

        mapAPi()

        Places.initialize(this@BookRideActivity, MAPS_API_KEY ?: "")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        subscribePlan = intent.getBooleanExtra("subscribePlan", false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        setOnClick()
    }




    fun mapAPi(){
        googleKeyViewModel = ViewModelProvider(this)[GetGoogleKeyViewModel::class.java]

        googleKeyViewModel.getGoogleKey()
        googleKeyViewModel.getGoogleKeyResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {

                }

                is BaseResponse.Success -> {
                    if (it.data!!.status) {
                        MAPS_API_KEY = it.data.data.google_key
                    }
                }

                else -> {
//                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setOnClick() {
        binding.imgMenu.setOnClickListener(this)
        binding.dropTextView.setOnClickListener(this)
        binding.txtSearch.setOnClickListener(this)
        binding.pickUpTextView.setOnClickListener(this)

    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    originLatitude = location.latitude
                    originLongitude = location.longitude
                    // Once you have the location, you can update the map

                    updateMapLocation()
                }
            }
    }

    private fun updateMapLocation() {
//        val latLng = LatLng(originLatitude, originLongitude)
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//        mMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
        val originLocation = LatLng(originLatitude.toDouble(), originLongitude.toDouble())
        mMap.addMarker(MarkerOptions().position(originLocation).title("Current Location"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15F))

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
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

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgMenu -> {
                startActivity(Intent(this, SideMenuActivity::class.java))
            }

            R.id.txtSearch -> {

                if (binding.pickUpTextView.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please select from location", Toast.LENGTH_SHORT).show()
                } else if (binding.dropTextView.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please select to location", Toast.LENGTH_SHORT).show()
                } else {


                    val bottomSheetFragment = ChooseVehicleBottomSheet(
                        binding.pickUpTextView.text.toString(),
                        binding.dropTextView.text.toString(),
                        pickup_lat,
                        pickup_lon,
                        drop_lat,
                        drop_long,
                        pickup_city,
                        subscribePlan
                    )
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

                }
            }

            R.id.pickUpTextView -> {
                onClickLocation(201)
            }

            R.id.dropTextView -> {
                onClickLocation(101)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun onClickLocation(i: Int) {
        REQUEST_AUTOCOMPLETE = i
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)
        val intent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, REQUEST_AUTOCOMPLETE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 201) {
            val place: Place = Autocomplete.getPlaceFromIntent(data!!)
            val latLng: LatLng? = place.latLng

            val myLat = latLng!!.latitude
            val myLong = latLng.longitude
            pickup_lat = myLat.toString()
            pickup_lon = myLong.toString()
            val geocoder = Geocoder(this@BookRideActivity, Locale.getDefault())
            try {
                val addresses: List<Address> = geocoder.getFromLocation(myLat, myLong, 1)!!
                state = addresses[0].adminArea
                pickup_city = addresses[0].locality
//                Toast.makeText(this, "address -> "+pickup_city, Toast.LENGTH_SHORT).show()
                country = addresses[0].countryName
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.pickUpTextView.text = place.name.toString().plus(",").plus(place.address.toString())
        }
        else if (resultCode == RESULT_OK && requestCode == 101) {
            val place: Place = Autocomplete.getPlaceFromIntent(data!!)
            val latLng: LatLng? = place.latLng

            val myLat = latLng!!.latitude
            val myLong = latLng.longitude
            drop_lat = myLat.toString()
            drop_long = myLong.toString()
            val geocoder = Geocoder(this@BookRideActivity, Locale.getDefault())
            try {
                val addresses: List<Address> = geocoder.getFromLocation(myLat, myLong, 1)!!
                state = addresses[0].adminArea
//                city = addresses[0].getLocality()
                country = addresses[0].countryName


            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.dropTextView.text = place.name.toString().plus(", ").plus(place.address.toString())
            updateMapLocation()
            mMap.clear()

            val destinationLocation = LatLng(drop_lat.toDouble(), drop_long.toDouble())
            mMap.addMarker(MarkerOptions().position(destinationLocation))

            val originLocation = LatLng(pickup_lat.toDouble(), pickup_lon.toDouble())
            mMap.addMarker(MarkerOptions().position(originLocation))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15F))
            val urll = getDirectionURL(originLocation, destinationLocation, MAPS_API_KEY ?: "")
            GetDirection(urll).execute()


        }


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


}


/*
package com.neplace.customer.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.neplace.customer.MapData
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityBookRideBinding
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.Locale

class BookRideActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    lateinit var binding: ActivityBookRideBinding
    var REQUEST_AUTOCOMPLETE = 201
    var PERMISSION_REQUEST_CODE = 200
    var state: String = ""
    var pickup_city: String = ""
    var country: String = ""
    lateinit var pickup_lat: String
    lateinit var pickup_lon: String
    lateinit var drop_lat: String
    lateinit var drop_long: String
    var apiKey: String = "AIzaSyAMAdPcBMgkpCDoycbvxeFh6274KbKCvjQ"

    private lateinit var mMap: GoogleMap
    private var originLatitude: Double = 0.0
    private var originLongitude: Double = 0.0
    private var destinationLatitude: Double = 0.0
    private var destinationLongitude: Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_ride)
        Places.initialize(this@BookRideActivity, "AIzaSyDybi97YEi_iux4SxEuNiTkOXEYs8SUy0g")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // Permissions already granted, get current location
            getCurrentLocation()
        }


        setOnClick()
    }


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                     originLatitude = location.latitude
                    originLongitude = location.longitude

//                    Toast.makeText(this, "Latitude: $originLatitude, Longitude: $originLongitude", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Unable to retrieve location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting location: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    private fun setOnClick() {
        binding.imgMenu.setOnClickListener(this)
        binding.dropTextView.setOnClickListener(this)
        binding.txtSearch.setOnClickListener(this)
        binding.pickUpTextView.setOnClickListener(this)
        binding.dropTextView.setOnClickListener(this)




        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyD4uc4XxRnaZaBABMJn9qWCFcmwWHDfFRE")
        }

        // Map Fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

            mapFragment.getMapAsync {
                mMap = it
                val originLocation = LatLng(originLatitude, originLongitude)
                mMap.addMarker(MarkerOptions().position(originLocation))
                val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
                mMap.addMarker(MarkerOptions().position(destinationLocation))
                val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
                GetDirection(urll).execute()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
            }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get current location
                getCurrentLocation()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgMenu -> {
                startActivity(Intent(this, SideMenuActivity::class.java))
            }

            R.id.txtSearch -> {
                if (binding.pickUpTextView.text.toString().isEmpty()) {
                    Toast.makeText(this, "Enter pickup location", Toast.LENGTH_SHORT).show()
                }
                else if (binding.dropTextView.text.toString().isEmpty()) {
                    Toast.makeText(this, "Enter pickup location", Toast.LENGTH_SHORT).show()
                } else {


                    val bottomSheetFragment = ChooseVehicleBottomSheet(
                        binding.pickUpTextView.text.toString(),
                        binding.dropTextView.text.toString(),
                        pickup_lat,
                        pickup_lon,
                        drop_lat,
                        drop_long,
                        pickup_city
                    )
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

//
//                    val intent = Intent(this, ChooseVehicleActivity::class.java)
//                    intent.putExtra("pickup_location", binding.pickUpTextView.text.toString())
//                    intent.putExtra("drop_location", binding.dropTextView.text.toString())
//                    intent.putExtra("pickup_lat", pickup_lat)
//                    intent.putExtra("pickup_long", pickup_lon)
//                    intent.putExtra("drop_lat", drop_lat)
//                    intent.putExtra("drop_long", drop_long)
//                    intent.putExtra("pickup_city", pickup_city.toString())
//                    startActivity(intent)
                }
            }

            R.id.pickUpTextView -> {
                onClickLocation(201)
            }

            R.id.dropTextView -> {
                onClickLocation(101)
            }
        }
    }

    fun onClickLocation(i: Int) {
        REQUEST_AUTOCOMPLETE = i
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)
        val intent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, REQUEST_AUTOCOMPLETE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 201) {
            val place: Place = Autocomplete.getPlaceFromIntent(data!!)
            val latLng: LatLng? = place.latLng

            val myLat = latLng!!.latitude
            val myLong = latLng!!.longitude
            pickup_lat = myLat.toString()
            pickup_lon = myLong.toString()
            val geocoder = Geocoder(this@BookRideActivity, Locale.getDefault())
            try {
                val addresses: List<Address> = geocoder.getFromLocation(myLat, myLong, 1)!!
                state = addresses[0].getAdminArea()
                pickup_city = addresses[0].getLocality()
//                Toast.makeText(this, "address -> "+pickup_city, Toast.LENGTH_SHORT).show()
                country = addresses[0].countryName
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.pickUpTextView.setText(
                place.name.toString().plus(",").plus(place.address.toString())
            )
        }
        else if (resultCode == RESULT_OK && requestCode == 101) {
            val place: Place = Autocomplete.getPlaceFromIntent(data!!)
            val latLng: LatLng? = place.latLng

            val myLat = latLng!!.latitude
            val myLong = latLng!!.longitude
            drop_lat = myLat.toString()
            drop_long = myLong.toString()
            val geocoder = Geocoder(this@BookRideActivity, Locale.getDefault())
            try {
                val addresses: List<Address> = geocoder.getFromLocation(myLat, myLong, 1)!!
                state = addresses[0].getAdminArea()
//                city = addresses[0].getLocality()
                country = addresses[0].countryName
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.dropTextView.setText(place.name.toString().plus(", ").plus(place.address.toString()))

        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0!!
        val originLocation = LatLng(originLatitude, originLongitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))

//        getURL(originLocation, LatLng(destinationLatitude, destinationLongitude))
        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5f)

        if (binding.pickUpTextView.text.isNotEmpty()){

            val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
            GetDirection(urll).execute()
        }

    }

//    private fun getURL(from: LatLng, to: LatLng): String {
//        val origin = "origin=" + from.latitude + "," + from.longitude
//        val dest = "destination=" + to.latitude + "," + to.longitude
//        val sensor = "sensor=false"
//        val params = "$origin&$dest&$sensor"
//        return "https://maps.googleapis.com/maps/api/directions/json?$params"
//    }


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
}*/
