package com.ogungor.nearplaces.Common

import com.ogungor.nearplaces.Model.Place
import com.ogungor.nearplaces.Remote.IGoogleAPIService
import com.ogungor.nearplaces.Remote.RetrofitClient
import com.ogungor.nearplaces.Remote.RetrofitScalarsClient

object Common {

    private val GOOGLE_API_URL="https://maps.googleapis.com/"

    var currentResult: Place?=null

    val googleAPIService:IGoogleAPIService
        get() = RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val googleAPIServiceScalars:IGoogleAPIService
        get() = RetrofitScalarsClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)


}