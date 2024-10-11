package com.neplace.customer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.viewmodel.GetGoogleKeyViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.neplace.customer.retrofit.RetrofitUtils.MAPS_API_KEY

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    private var originLatitude: Double = 30.7046
    private var originLongitude: Double = 76.7179
    private var destinationLatitude: Double = 30.3398
    private var destinationLongitude: Double = 76.3869
    lateinit var apiKey: String
    private lateinit var googleKeyViewModel: GetGoogleKeyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hitMainAPi()

    }

    fun hitMainAPi() {

        googleKeyViewModel = ViewModelProvider(this)[GetGoogleKeyViewModel::class.java]

        googleKeyViewModel.getGoogleKey()

        googleKeyViewModel.getGoogleKeyResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {

                }

                is BaseResponse.Success -> {
                    if (it.data!!.status) {
                        MAPS_API_KEY = it.data.data.google_key
                        apiKey = it.data.data.google_key

                        val mapFragment =
                            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                        mapFragment.getMapAsync(this)

                        Places.initialize(this@MainActivity, it.data.data.google_key)


                    }
                }


                else -> {
                    Toast.makeText(this@MainActivity, "error main", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add markers
        val sydney = LatLng(originLatitude, originLongitude)
        val opera = LatLng(destinationLatitude, destinationLatitude)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.addMarker(MarkerOptions().position(opera).title("Opera House"))

        var polylineOptions = PolylineOptions().apply {
            add(sydney)
            add(opera)
        }

        val origin = "$originLatitude,$originLongitude"
        val destination = "$destinationLatitude,$destinationLongitude"

        mMap!!.addPolyline(polylineOptions)

        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        val directionsResult: DirectionsResult = DirectionsApi.newRequest(geoApiContext)
            .origin(origin)
            .destination(destination)
            .await()

        val polyline = directionsResult.routes[0].overviewPolyline
        val path = polyline.decodePath()

// Convert the path to a list of LatLng points
        val latLngList = path.map { LatLng(it.lat, it.lng) }

// Print the coordinates
        for (latLng in latLngList) {
//            println("Latitude: ${latLng.lat}, Longitude: ${latLng.lng}")
        }
    }


}