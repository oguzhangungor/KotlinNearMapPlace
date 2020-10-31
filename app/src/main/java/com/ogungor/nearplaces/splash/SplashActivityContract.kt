package com.ogungor.nearplaces.splash

interface SplashActivityContract {

    interface Presenter{
        fun setView(view : View)

        fun destroy()

        fun create()

        fun handlerFinished()

        fun checkLocationPermission()









    }

    interface View{

        fun initUi()

        fun startGifAnim()

        fun startHandler()

        fun intentToLocationPermissionActivity()

        fun intentToMainActivity()

        fun finishCurrentActivity()







    }

}