package com.ogungor.kotlinnearmapplace.splash

interface SplashActivityContract {

    interface Presenter{
        fun setView(view : View)

        fun destroy()

        fun create()

        fun handlerFinised()

        fun checkLocationPermission()







    }

    interface View{

        fun initUi()

        fun startGifAnim()

        fun startHandler()

        fun intentToMapsActivity()

        fun intentToLocationPermissionActivity()

        fun finishCurrentActivity()







    }

}