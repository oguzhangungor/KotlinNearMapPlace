package com.ogungor.kotlinnearmapplace.main

import android.location.Location
import com.ogungor.kotlinnearmapplace.Model.MyPlaces
import com.ogungor.kotlinnearmapplace.Remote.IGoogleAPIService
import com.ogungor.kotlinnearmapplace.enum.PlaceType
import com.ogungor.kotlinnearmapplace.util.urlprocess.UrlProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivityPresenter(private val service:IGoogleAPIService,private val urlProvider: UrlProvider) : MainActivityContract.Presenter {

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
      val url=urlProvider.getUrl(location.latitude,location.longitude,placeType.toString())
        service.getNearbyPlaces(url)

            .enqueue(object : Callback<MyPlaces>
            {
                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                   response.body()?.results?.let { placeList ->
                       view?.run{
                           showPlace(placeList, placeType)
                           showLocation(location)
                       }


                   }
                }


            }
            )




    }


}