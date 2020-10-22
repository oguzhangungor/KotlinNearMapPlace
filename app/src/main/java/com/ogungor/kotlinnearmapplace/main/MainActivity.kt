package com.ogungor.kotlinnearmapplace.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ogungor.kotlinnearmapplace.Common.Common
import com.ogungor.kotlinnearmapplace.Model.MyPlaces
import com.ogungor.kotlinnearmapplace.R
import com.ogungor.kotlinnearmapplace.Remote.IGoogleAPIService

class MainActivity : AppCompatActivity(), OnMapReadyCallback , MainActivityContract.View{

    private lateinit var mMap: GoogleMap

    lateinit var mService: IGoogleAPIService

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    internal lateinit var currentPlace: MyPlaces

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    override fun initUi() {

    }

    override fun stopLocation() {

        super.onStop()
    }
}