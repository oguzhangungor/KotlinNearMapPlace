package com.ogungor.kotlinnearmapplace.util

interface RunTimePermissionListener {

    val hasAccessFineLocation: Boolean

    fun getAccessFineLocationPermission()


}