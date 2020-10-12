package com.ogungor.kotlinnearmapplace

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ogungor.kotlinnearmapplace.Common.Common
import com.ogungor.kotlinnearmapplace.Model.MyPlaces
import com.ogungor.kotlinnearmapplace.Remote.IGoogleAPIService
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap

    private var latitude = 0.toDouble()
    private var longitude = 0.toDouble()

    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null


    //Location

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback


    companion object {
        private val MY_PERMISSION_CODE = 1
    }

    lateinit var mService: IGoogleAPIService

    internal lateinit var currentPlace: MyPlaces


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Inıt Service

        mService = Common.googleAPIService


        //Request runtime Permission

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallBack()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        } else {
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.myLooper()
            )
        }

        bottom_navigator_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_hospital -> nearByPlace("hospital")
                R.id.action_market -> nearByPlace("market")
                R.id.action_restaurant -> nearByPlace("restaurant")
                R.id.school -> nearByPlace("school")
            }
            true
        }


    }

    private fun nearByPlace(typePlace: String) {

        //Clear All Marker on Map
        mMap.clear()

        //Build URL request base on location

        val url = getUrl(latitude, longitude, typePlace)

        mService.getNearbyPlaces(url)
            .enqueue(object : Callback<MyPlaces> {
                override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                    currentPlace = response.body()!!

                    if (response!!.isSuccessful) {

                        for (i in 0 until response!!.body()!!.results!!.size) {
                            val markerOptios = MarkerOptions()
                            val googlePlace = response.body()!!.results!![i]
                            val lat = googlePlace.geometry!!.location!!.lat
                            val lng = googlePlace.geometry!!.location!!.lng
                            val placeName = googlePlace.name
                            val latLng = LatLng(lat, lng)


                            markerOptios.position(latLng)
                            markerOptios.title(placeName)

                            if (typePlace.equals("hospital")) {
                                markerOptios.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hospital))
                            } else if (typePlace.equals("market")) {
                                markerOptios.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shopping))
                            } else if (typePlace.equals("restaurant")) {
                                markerOptios.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant))
                            } else if (typePlace.equals("school")) {
                                markerOptios.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school))
                            } else {
                                markerOptios.icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED
                                    )
                                )

                            }
                           /* if (typePlace.equals("hospital")) {
                                markerOptios.icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED
                                    )
                                )
                            } else if (typePlace.equals("market")) {
                                markerOptios.icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED
                                    )
                                )
                            } else if (typePlace.equals("restaurant")) {
                                markerOptios.icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED
                                    )
                                )
                            } else if (typePlace.equals("school")) {
                                markerOptios.icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED
                                    )
                                )
                            } else {
                                markerOptios.icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED
                                    )
                                )

                            }*/
                            markerOptios.snippet(typePlace.toString())  //Alt Bilgi

                            //Add marker to map

                            mMap.addMarker(markerOptios)


                        }
                        // Move Camera Zoom

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude,longitude), 16f))
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(17f))


                    }
                }

                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    (Toast.makeText(baseContext, "..." + t.message, Toast.LENGTH_LONG).show())
                }


            })


    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {

        val googlePlaceUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=300") // 10km aralığında
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyBLWNF4ChIdv8co_K8vtXsd3d3_z6DDrLc")

        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()
    }

    private fun buildLocationCallBack() {

        // Son lokasyonu alıyoruz
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult?) {
                mLastLocation = location!!.locations.get(location!!.locations.size - 1) //Get Last Location
                if (mMarker != null) {
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLang = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLang)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMarker = mMap!!.addMarker(markerOptions)


                //Move Camera zoom

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, 17f))

                // https://www.youtube.com/watch?v=SHn-K37zOZU&ab_channel=EDMTDev


            }
        }

    }


    @SuppressLint("RestrictedApi")
    private fun buildLocationRequest() {

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }



    //Kullanıcının iznini kontrol ediyoruz bunu splash ekranında yapacağız.
    private fun checkLocationPermission(): Boolean {



        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            else
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )


            return false
        } else
            return true

    }


//Override OnRequestPermissionResult /Kullanıcı izin vermiş mi vermemiş mi

    override fun onRequestPermissionsResult(


        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (checkLocationPermission()) {

                            buildLocationRequest()
                            buildLocationCallBack()


                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest, locationCallback,
                                Looper.myLooper()
                            )

                            mMap.isMyLocationEnabled = true
                        }
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Init Google Play Service

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true
            }
        } else
            mMap.isMyLocationEnabled = true


        //enable Zoom Control

        mMap.uiSettings.isZoomControlsEnabled = true


    }
}