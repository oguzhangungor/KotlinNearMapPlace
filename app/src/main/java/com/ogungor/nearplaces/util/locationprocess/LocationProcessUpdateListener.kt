package com.ogungor.nearplaces.util.locationprocess

import android.location.Location

interface LocationProcessUpdateListener {

    fun onLocationChanged(location: Location)
    fun onFailed()
}