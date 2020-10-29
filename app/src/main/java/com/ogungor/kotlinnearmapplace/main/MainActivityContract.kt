package com.ogungor.kotlinnearmapplace.main

import android.location.Location
import com.ogungor.kotlinnearmapplace.Model.Place
import com.ogungor.kotlinnearmapplace.enum.PlaceType

interface MainActivityContract {

    interface Presenter {

        fun setView(view : View)

        fun create ()

        fun destroy()

        fun locationChange(location : Location)

        fun getNearPlaces(location: Location, placeType: PlaceType)

        fun bottomNavigationClick(itemId:Int)
    }

    interface View {

        fun initUi()

        fun initListeners()

        fun showPlace(placeList: Array<Place>, placeType: PlaceType)

        fun showLocation (location: Location)

        fun stopLocation()

        fun mapClear()

        fun showGeneralFailedToast()


    }


}