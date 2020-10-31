package com.ogungor.nearplaces.util

interface RunTimePermissionListener {

    val hasAccessFineLocation: Boolean

    fun getAccessFineLocationPermission()
}