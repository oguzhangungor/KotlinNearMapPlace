package com.ogungor.kotlinnearmapplace.util.locationprocess

import android.location.Location

interface LocationProcessUpdateListener {

    fun onLocationChanged(location: Location)


    fun onFailed()
}