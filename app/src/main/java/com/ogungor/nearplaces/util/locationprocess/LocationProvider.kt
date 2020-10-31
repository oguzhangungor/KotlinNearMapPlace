package com.ogungor.nearplaces.util.locationprocess

interface LocationProvider {

    fun getLastLocation(locationProcessUpdateListener: LocationProcessUpdateListener)

    fun getCurrentLocation(locationProcessUpdateListener: LocationProcessUpdateListener)
}