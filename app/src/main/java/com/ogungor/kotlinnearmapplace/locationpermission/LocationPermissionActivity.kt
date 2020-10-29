package com.ogungor.kotlinnearmapplace.locationpermission

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ogungor.kotlinnearmapplace.R
import com.ogungor.kotlinnearmapplace.base.BaseActivity
import com.ogungor.kotlinnearmapplace.splash.SplashActivityContract
import com.ogungor.kotlinnearmapplace.util.RunTimePermissionHelper
import com.ogungor.kotlinnearmapplace.util.RunTimePermissionListener
import com.ogungor.kotlinnearmapplace.util.extention.startMainActivity
import com.ogungor.kotlinnearmapplace.util.extention.startMapsActivity
import java.util.jar.Manifest

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
                   val isChosenJustDeny= shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    if(!isChosenJustDeny){
                        AlertDialog.Builder(this).apply {
                            setTitle("Lokasyon İzniniz Kapalı")
                            setMessage("Size daha iyi hizmet verebilmemiz için lokasyon iznini konumlardan açınız")
                            setPositiveButton(
                                "Konum Aç"
                            ) { p0, p1 ->


                            }
                            setNegativeButton(
                                "Daha Sonra"
                            ) { p0, p1 ->


                            }
                            show()
                        }
                    }
                    else
                    {
                        locationPermissionPresenter.accessFineLocationFailed()
                    }
                }


            }
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