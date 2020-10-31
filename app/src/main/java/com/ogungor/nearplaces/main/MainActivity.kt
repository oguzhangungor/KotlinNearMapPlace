package com.ogungor.nearplaces.main

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.ogungor.nearplaces.common.Common
import com.ogungor.nearplaces.model.Place
import com.ogungor.nearplaces.R
import com.ogungor.nearplaces.base.BaseActivity
import com.ogungor.nearplaces.enum.PlaceType
import com.ogungor.nearplaces.util.extention.showToast
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

    lateinit var progressBar: RelativeLayout

    lateinit var lottieView: LottieAnimationView


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
        disableMap()
        mMap.isMyLocationEnabled=true
        locationProvider?.getLastLocation(object : LocationProcessUpdateListener{
            override fun onLocationChanged(location: Location) {
                mainActivityPresenter.locationChange(location)


            }

            override fun onFailed() {
            }

        })

    }

    override fun enableMap() {
        mMap.uiSettings.setAllGesturesEnabled(true)
    }

    override fun disableMap() {
        mMap.uiSettings.setAllGesturesEnabled(false)

    }

    override fun initUi() {
        bottomNavigator=findViewById(R.id.bottom_navigator_view)
        progressBar=findViewById(R.id.progressBar)
        lottieView=findViewById(R.id.lottieView)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)





    }

    override fun showProgress() {
        lottieView.playAnimation()
        progressBar.visibility=View.VISIBLE
    }

    override fun hideProgress() {
        lottieView.pauseAnimation()
        progressBar.visibility=View.GONE

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
       showToast(getString(R.string.toast_failed_message))
    }

    override fun showEmptyToast(placeType: PlaceType) {
        showToast(getString(R.string.toast_empty_near_message,placeType.typeValue))

    }

}