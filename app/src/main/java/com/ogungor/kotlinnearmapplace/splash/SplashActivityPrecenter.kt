package com.ogungor.kotlinnearmapplace.splash

import com.ogungor.kotlinnearmapplace.util.RunTimePermissionListener

class SplashActivityPresenter(private val runTimePermissionListener: RunTimePermissionListener) :
    SplashActivityContract.Presenter {


    private var view: SplashActivityContract.View? = null

    override fun setView(view: SplashActivityContract.View) {
        this.view
    }

    override fun destroy() {
        view = null
    }

    override fun create() {

        view?.run {
            initUi()
            startGifAnim()
        }

    }

    override fun handlerFinised() {
        checkLocationPermission()
    }

    override fun checkLocationPermission() {
        if (runTimePermissionListener.hasAccessFineLocation) {
            view?.intentToMapsActivity()
        } else {
            view?.intentToLocationPermissionActivity()

        }
        view?.finishCurrentActivity()
    }


}