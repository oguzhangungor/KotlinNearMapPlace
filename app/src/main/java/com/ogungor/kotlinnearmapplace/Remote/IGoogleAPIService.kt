package com.ogungor.kotlinnearmapplace.Remote

import com.google.firebase.appindexing.builders.StickerBuilder
import com.ogungor.kotlinnearmapplace.Model.MyPlaces
import com.ogungor.kotlinnearmapplace.Model.PlaceDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearbyPlaces(@Url url:String) : Call<MyPlaces>

    @GET
    fun getDetailPlace(@Url url:String) : Call<PlaceDetail>

    @GET ("maps/api/direction/json")
    fun getDirection(@Query ("origin") origin:String, @Query("destiation")  destination: String) : Call<String>




}