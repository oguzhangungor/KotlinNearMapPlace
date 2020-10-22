package com.ogungor.kotlinnearmapplace.util.extention

import android.app.Activity
import android.content.Intent
import com.ogungor.kotlinnearmapplace.MapsActivity
import com.ogungor.kotlinnearmapplace.locationpermission.LocationPermissionActivity
import com.ogungor.kotlinnearmapplace.main.MainActivity

fun Activity.startMapsActivity(){
    this.startActivity(Intent(this,MapsActivity::class.java))
}

fun Activity.startLocationPermissionActivity(){
    this.startActivity(Intent(this,LocationPermissionActivity::class.java))
}

fun Activity.startMainActivity(){
    this.startActivity(Intent(this,MainActivity::class.java))

}