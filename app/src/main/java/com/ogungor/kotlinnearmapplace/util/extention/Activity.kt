package com.ogungor.kotlinnearmapplace.util.extention

import android.app.Activity
import android.content.Intent
import com.ogungor.kotlinnearmapplace.MapsActivity
import com.ogungor.kotlinnearmapplace.locationpermission.LocationPermissionActivty

fun Activity.startMapsActivity(){
    this.startActivity(Intent(this,MapsActivity::class.java))
}

fun Activity.startLocationPermissionActivity(){
    this.startActivity(Intent(this,LocationPermissionActivty::class.java))
}