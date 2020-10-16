package com.ogungor.kotlinnearmapplace.util.extention

import android.app.Activity
import android.content.Intent
import com.ogungor.kotlinnearmapplace.MapsActivity
import com.ogungor.kotlinnearmapplace.locationpermission.LocationPermissionActivity

fun Activity.startMapsActivity(){
    this.startActivity(Intent(this,MapsActivity::class.java))
}

fun Activity.startLocationPermissionActivity(){
    this.startActivity(Intent(this,LocationPermissionActivity::class.java))
}