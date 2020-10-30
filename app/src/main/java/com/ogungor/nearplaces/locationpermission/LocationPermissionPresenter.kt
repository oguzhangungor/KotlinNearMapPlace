package com.ogungor.nearplaces.locationpermission

import com.ogungor.nearplaces.util.RunTimePermissionListener

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
        view?.showToastFailedMessage()

    }


}