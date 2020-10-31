package com.ogungor.nearplaces.locationpermission

interface LocationPermissionActivityContract {

    interface Presenter{

        fun setView(view : View)

        fun create()

        fun destroy()

        fun requesPermissionClick()

        fun accessFineLocationSuccess()

        fun accessFineLocationFailed()
    }


    interface View{

        fun intentToMainActivity()

        fun showToastFailedMessage()

        fun showLocationPermissionDialog()
    }
}