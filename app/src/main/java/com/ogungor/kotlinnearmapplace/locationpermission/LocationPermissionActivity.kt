package com.ogungor.kotlinnearmapplace.locationpermission

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ogungor.kotlinnearmapplace.R
import com.ogungor.kotlinnearmapplace.base.BaseActivity
import com.ogungor.kotlinnearmapplace.splash.SplashActivityContract
import com.ogungor.kotlinnearmapplace.util.RunTimePermissionHelper
import com.ogungor.kotlinnearmapplace.util.RunTimePermissionListener
import com.ogungor.kotlinnearmapplace.util.extention.startMainActivity
import com.ogungor.kotlinnearmapplace.util.extention.startMapsActivity

class LocationPermissionActivity : BaseActivity(), LocationPermissionActivityContract.View {

    private lateinit var locationPermissionPresenter: LocationPermissionActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_permission)

        var runTimePermissionListener = RunTimePermissionHelper(this)

        locationPermissionPresenter = LocationPermissionPresenter(runTimePermissionListener).apply {
            setView(this@LocationPermissionActivity)
            create()

        }

    }

    override fun getLayout(): Int =R.layout.activity_location_permission

    fun locationPermission(view:View) {

        locationPermissionPresenter.requesPermissionClick()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RunTimePermissionHelper.REQUEST_PERMISSION_ACCESS_FINE_LOCATION &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ){
            locationPermissionPresenter.accessFineLocationSuccess()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationPermissionPresenter.destroy()
    }

    override fun intentToMainActivity() {
        startMainActivity()
        finish()
    }


}