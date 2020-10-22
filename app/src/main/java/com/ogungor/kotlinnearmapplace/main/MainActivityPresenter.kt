package com.ogungor.kotlinnearmapplace.main

import android.location.Location
import com.ogungor.kotlinnearmapplace.enum.PlaceType


class MainActivityPresenter : MainActivityContract.Presenter {
    private var placeType:PlaceType=PlaceType.MARKET
    private var view: MainActivityContract.View?=null
    override fun setView(view : MainActivityContract.View) {
        this.view=view
    }

    override fun create() {
        view?.run {
            initUi()
        }
    }

    override fun destroy() {
        view=null
    }

    override fun locationChange(location: Location) {
        getNearPlaces(location,placeType)
    }

    override fun getNearPlaces(location: Location, placeType: PlaceType) {
        
    }


}