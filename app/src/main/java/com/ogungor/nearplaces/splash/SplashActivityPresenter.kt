package com.ogungor.nearplaces.splash

import com.ogungor.nearplaces.util.RunTimePermissionListener

class SplashActivityPresenter(private val runTimePermissionListener: RunTimePermissionListener) :
    SplashActivityContract.Presenter {


    private var view: SplashActivityContract.View? = null

    override fun setView(view: SplashActivityContract.View) {
        this.view=view
    }

    override fun destroy() {
        view = null
    }

    override fun create() {

        view?.run {
            initUi()
            startGifAnim()
            startHandler()
        }

    }

    override fun handlerFinished() {
        checkLocationPermission()
    }

    override fun checkLocationPermission() {
        if (runTimePermissionListener.hasAccessFineLocation) {
            view?.intentToMainActivity()
        } else {
            view?.intentToLocationPermissionActivity()

        }
        view?.finishCurrentActivity()
    }


}