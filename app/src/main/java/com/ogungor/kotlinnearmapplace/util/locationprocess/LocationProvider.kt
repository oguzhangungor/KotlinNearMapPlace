package com.ogungor.kotlinnearmapplace.util.locationprocess

interface LocationProvider {

    fun getLastLocation(locationProcessUpdateListener: LocationProcessUpdateListener)


    fun getCurrentLocation(locationProcessUpdateListener: LocationProcessUpdateListener)
}