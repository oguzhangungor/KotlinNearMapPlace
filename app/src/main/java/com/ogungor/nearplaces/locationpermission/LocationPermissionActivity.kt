package com.ogungor.nearplaces.locationpermission

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ogungor.nearplaces.R
import com.ogungor.nearplaces.base.BaseActivity
import com.ogungor.nearplaces.util.RunTimePermissionHelper
import com.ogungor.nearplaces.util.extention.startAppSettings
import com.ogungor.nearplaces.util.extention.startMainActivity

class LocationPermissionActivity : BaseActivity(), LocationPermissionActivityContract.View {

    private lateinit var locationPermissionPresenter: LocationPermissionActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_permission)

        val runTimePermissionListener = RunTimePermissionHelper(this)

        locationPermissionPresenter = LocationPermissionPresenter(runTimePermissionListener).apply {
            setView(this@LocationPermissionActivity)
            create()
        }
    }

    override fun getLayout(): Int = R.layout.activity_location_permission

    fun locationPermission(view: View) {
        locationPermissionPresenter.requesPermissionClick()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RunTimePermissionHelper.REQUEST_PERMISSION_ACCESS_FINE_LOCATION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionPresenter.accessFineLocationSuccess()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val isChosenJustDeny =
                        shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    if (!isChosenJustDeny) {
                        showLocationPermissionDialog()
                    } else {
                        locationPermissionPresenter.accessFineLocationFailed()
                    }
                }
            }
        }
    }

    override fun showToastFailedMessage() {
        Toast.makeText(this, R.string.toast_failed_message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationPermissionPresenter.destroy()
    }

    override fun intentToMainActivity() {
        startMainActivity()
        finish()
    }

    override fun showLocationPermissionDialog() {
        AlertDialog.Builder(this, R.style.MyAlertDialogStyle).apply {
            setTitle(R.string.location_dialog_title)
            setMessage(R.string.location_dialog_message)
            setPositiveButton(
                R.string.location_dialog_positive_button_text
            ) { p0, p1 ->
                startAppSettings()
            }
            setNegativeButton(
                R.string.location_dialog_negative_button_text
            ) { p0, p1 ->
                locationPermissionPresenter.accessFineLocationFailed()
            }
            show()
        }
    }
}