package com.ogungor.kotlinnearmapplace.main

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

import com.ogungor.kotlinnearmapplace.Common.Common
import com.ogungor.kotlinnearmapplace.Model.Place
import com.ogungor.kotlinnearmapplace.R
import com.ogungor.kotlinnearmapplace.base.BaseActivity
import com.ogungor.kotlinnearmapplace.enum.PlaceType
import com.ogungor.kotlinnearmapplace.util.locationprocess.GmsLocationProvider
import com.ogungor.kotlinnearmapplace.util.locationprocess.LocationProcessUpdateListener
import com.ogungor.kotlinnearmapplace.util.locationprocess.LocationProvider
import com.ogungor.kotlinnearmapplace.util.markerprocess.MarkerProvider
import com.ogungor.kotlinnearmapplace.util.urlprocess.UrlProvider

class MainActivity : BaseActivity(), OnMapReadyCallback, MainActivityContract.View {

    private lateinit var mMap: GoogleMap

    private var locationProvider: LocationProvider? = null

    private  lateinit var markerProvider: MarkerProvider


    lateinit var mainActivityPresenter: MainActivityContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val service=Common.googleAPIService
        val urlProvider=UrlProvider()

        locationProvider=GmsLocationProvider(this)

        mainActivityPresenter = MainActivityPresenter(service,urlProvider).apply {
            setView(this@MainActivity)
            create()


        }

    }

    override fun getLayout(): Int = R.layout.activity_main

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationProvider?.getCurrentLocation(object : LocationProcessUpdateListener{
            override fun onLocationChanged(location: Location) {
                mainActivityPresenter.locationChange(location)
            }

            override fun onFailed() {
                TODO("Not yet implemented")
            }

        })

    }

    override fun initUi() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun showPlace(placeList: Array<Place>,placeType: PlaceType) {

        for (place in placeList){
            mMap.addMarker(markerProvider.getRelatedMarker(place,placeType))
        }


    }

    override fun showLocation(location: Location) {
        var latLng=LatLng(location.latitude,location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14f))
    }

    override fun stopLocation() {

        super.onStop()
    }
}