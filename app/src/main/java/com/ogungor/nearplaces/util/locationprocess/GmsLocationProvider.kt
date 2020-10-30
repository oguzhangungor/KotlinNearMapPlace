package com.ogungor.nearplaces.util.locationprocess

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import com.ogungor.nearplaces.base.BaseActivity

class GmsLocationProvider(private val activity: BaseActivity) : LocationProvider {


    private var locationManager: LocationManager =
        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val LOCATION_REFRESH_TIME = 10000L
    private val LOCATION_REFRESH_DISTANCE = 10F


    @SuppressLint("MissingPermission")
    override fun getLastLocation(locationProcessUpdateListener: LocationProcessUpdateListener) {
        LocationServices.getFusedLocationProviderClient(activity)
            .lastLocation.addOnSuccessListener { location ->
                locationProcessUpdateListener.onLocationChanged(location)

            }.addOnFailureListener {
                locationProcessUpdateListener.onFailed()
            }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(locationProcessUpdateListener: LocationProcessUpdateListener) {
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                locationProcessUpdateListener.onLocationChanged(p0)
            }
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE,
            locationListener
        )


    }
}