package com.ogungor.nearplaces.main

import android.location.Location
import com.ogungor.nearplaces.Model.MyPlaces
import com.ogungor.nearplaces.R
import com.ogungor.nearplaces.Remote.IGoogleAPIService
import com.ogungor.nearplaces.enum.PlaceType
import com.ogungor.nearplaces.util.urlprocess.UrlProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivityPresenter(
    private val service: IGoogleAPIService,
    private val urlProvider: UrlProvider
) : MainActivityContract.Presenter {

    private var placeType: PlaceType = PlaceType.MARKET

    private var location: Location? = null

    private var view: MainActivityContract.View? = null

    override fun setView(view: MainActivityContract.View) {
        this.view = view
    }

    override fun create() {
        view?.run {
            initUi()
            initListeners()
        }
    }

    override fun destroy() {
        view = null
    }

    override fun locationChange(location: Location) {
        this.location = location
        getNearPlaces(location, placeType)
    }

    override fun getNearPlaces(location: Location, placeType: PlaceType) {

        val url = urlProvider.getUrl(location.latitude, location.longitude, placeType.typeValue)
        service.getNearbyPlaces(url)

            .enqueue(object : Callback<MyPlaces> {
                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    view?.showGeneralFailedToast()
                }

                override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                    response.body()?.results?.let { placeList ->
                        if(placeList.isEmpty()){
                            view?.emptyNearPlace(placeType)
                        }else{
                            view?.run {
                                mapClear()
                                showPlace(placeList, placeType)
                                showLocation(location)

                            }
                        }


                    }
                }


            }
            )


    }

    override fun bottomNavigationClick(id: Int) {
        location?.let {
            var placeType = when (id) {
                R.id.action_hospital -> PlaceType.HASTANE
                R.id.school -> PlaceType.OKUL
                R.id.action_market -> PlaceType.MARKET
                R.id.action_restaurant -> PlaceType.RESTORAN
                else -> PlaceType.RESTORAN
            }
            getNearPlaces(it, placeType)
        }
    }


}