package com.ogungor.kotlinnearmapplace

import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private  var latitude=0.toDouble()
    private  var longitude=0.toDouble()

    private lateinit var mLastLocation:Location
    private var mMarker:Marker?=null


    //Location

    lateinit var fusedLocationLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        
        //Request runtime Permission
        
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            checkLocationPermission()
            buildLocationRequest()
            buildLocationCallBack()
            
            
            fusedLocationLocationClient= LocationServices.getFusedLocationProviderClient(this)
            fusedLocationLocationClient.requestLocationUpdates(locationRequest,locationCallback,
                Looper.myLooper())


            
            
        }

        
    }

    private fun buildLocationCallBack() {

        locationCallback= object : LocationCallback() {
            override fun onLocationResult(loc: LocationResult?) {
                mLastLocation = loc!!.locations.get(loc!!.locations.size-1) //Get Last Location
                if (mMarker !=null){
                    mMarker!!.remove()
                }

                latitude= mLastLocation.latitude
                longitude= mLastLocation.longitude


                //Video da 07:07 deyiz

                // https://www.youtube.com/watch?v=SHn-K37zOZU&ab_channel=EDMTDev


            }
        }
        
    }

    private fun buildLocationRequest() {
        
    }

    private fun checkLocationPermission() {
        
        
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}