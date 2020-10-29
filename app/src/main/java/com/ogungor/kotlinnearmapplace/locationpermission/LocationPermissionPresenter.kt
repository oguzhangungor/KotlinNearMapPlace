package com.ogungor.kotlinnearmapplace.locationpermission

import android.util.Log
import com.ogungor.kotlinnearmapplace.util.RunTimePermissionListener

class LocationPermissionPresenter (private var runTimePermissionListener: RunTimePermissionListener): LocationPermissionActivityContract.Presenter{

    private var view:LocationPermissionActivityContract.View?=null

    override fun setView(view: LocationPermissionActivityContract.View) {

        this.view=view
    }

    override fun create() {


    }

    override fun destroy() {
       view=null
    }

    override fun requesPermissionClick() {
        runTimePermissionListener.getAccessFineLocationPermission()
    }

    override fun accessFineLocationSuccess() {
        view?.intentToMainActivity()
    }

    override fun accessFineLocationFailed() {
        Log.d("fineLocation","Not Location")
    }


}