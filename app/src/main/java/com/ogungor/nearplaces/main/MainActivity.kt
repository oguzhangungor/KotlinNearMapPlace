package com.ogungor.nearplaces.main

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.ogungor.nearplaces.common.Common
import com.ogungor.nearplaces.model.Place
import com.ogungor.nearplaces.R
import com.ogungor.nearplaces.base.BaseActivity
import com.ogungor.nearplaces.enum.PlaceType
import com.ogungor.nearplaces.util.locationprocess.GmsLocationProvider
import com.ogungor.nearplaces.util.locationprocess.LocationProcessUpdateListener
import com.ogungor.nearplaces.util.locationprocess.LocationProvider
import com.ogungor.nearplaces.util.markerprocess.MarkerProvider
import com.ogungor.nearplaces.util.urlprocess.UrlProvider

class MainActivity : BaseActivity(), OnMapReadyCallback, MainActivityContract.View {

    private lateinit var mMap: GoogleMap

    private var locationProvider: LocationProvider? = null

    private  lateinit var markerProvider: MarkerProvider

    lateinit var bottomNavigator:BottomNavigationView


    lateinit var mainActivityPresenter: MainActivityContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val service=Common.googleAPIService
        val urlProvider=UrlProvider()

        markerProvider= MarkerProvider()

        locationProvider=GmsLocationProvider(this)

        mainActivityPresenter = MainActivityPresenter(service,urlProvider).apply {
            setView(this@MainActivity)
            create()


        }

    }

    override fun getLayout(): Int = R.layout.activity_main

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled=true
        locationProvider?.getCurrentLocation(object : LocationProcessUpdateListener{
            override fun onLocationChanged(location: Location) {
                mainActivityPresenter.locationChange(location)


            }

            override fun onFailed() {
            }

        })

    }

    override fun initUi() {
        bottomNavigator=findViewById(R.id.bottom_navigator_view)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun initListeners() {
        bottomNavigator.setOnNavigationItemSelectedListener { item ->
            mainActivityPresenter.bottomNavigationClick(item.itemId)
            true
        }
    }

    override fun showPlace(placeList: Array<Place>,placeType: PlaceType) {

        for (place in placeList){
            mMap.addMarker(markerProvider.getRelatedMarker(place,placeType))
        }


    }

    override fun showLocation(location: Location) {
        var latLng=LatLng(location.latitude,location.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f))

    }

    override fun stopLocation() {

        super.onStop()
    }

    override fun mapClear() {
        mMap.clear()
    }

    override fun showGeneralFailedToast() {
        Toast.makeText(this,R.string.toast_failed_message,Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StringFormatMatches")
    override fun emptyNearPlace(placeType: PlaceType) {
        Toast.makeText(this,resources.getString(R.string.toast_empyt_near_message,placeType),Toast.LENGTH_LONG).show()
    }

}