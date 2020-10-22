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
import com.ogungor.kotlinnearmapplace.base.BaseActivity

class MainActivity : BaseActivity(), OnMapReadyCallback , MainActivityContract.View{

    private lateinit var mMap: GoogleMap


    lateinit var mainActivityPresenter: MainActivityContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityPresenter= MainActivityPresenter().apply {
            setView(this@MainActivity)
            create()
        }

    }

    override fun getLayout(): Int =R.layout.activity_main

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    override fun initUi() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun stopLocation() {

        super.onStop()
    }
}