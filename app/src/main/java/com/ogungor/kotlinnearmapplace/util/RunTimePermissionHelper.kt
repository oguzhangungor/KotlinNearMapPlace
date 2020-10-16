package com.ogungor.kotlinnearmapplace.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class RunTimePermissionHelper(private val context: AppCompatActivity) : RunTimePermissionListener {
    companion object {
        const val REQUEST_PERMISSOIN_ACCESS_FINE_LOCATION = 100
    }

    override val hasAccessFineLocation: Boolean
        get() = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    override fun getAccessFineLocationPermission() {
        ActivityCompat.requestPermissions(
            context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSOIN_ACCESS_FINE_LOCATION
        )
    }
}