package com.ogungor.nearplaces.common

import com.ogungor.nearplaces.model.Place
import com.ogungor.nearplaces.remote.IGoogleAPIService
import com.ogungor.nearplaces.remote.RetrofitClient
import com.ogungor.nearplaces.remote.RetrofitScalarsClient

object Common {

    private val GOOGLE_API_URL="https://maps.googleapis.com/"

    var currentResult: Place?=null

    val googleAPIService:IGoogleAPIService
        get() = RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val googleAPIServiceScalars:IGoogleAPIService
        get() = RetrofitScalarsClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)


}