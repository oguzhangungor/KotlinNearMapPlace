package com.ogungor.kotlinnearmapplace.Common

import com.ogungor.kotlinnearmapplace.Model.Results
import com.ogungor.kotlinnearmapplace.Remote.IGoogleAPIService
import com.ogungor.kotlinnearmapplace.Remote.RetrofitClient
import com.ogungor.kotlinnearmapplace.Remote.RetrofitScalarsClient

object Common {

    private val GOOGLE_API_URL="https://maps.googleapis.com/"

    var currentResult: Results?=null

    val googleAPIService:IGoogleAPIService
        get() = RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val googleAPIServiceScalars:IGoogleAPIService
        get() = RetrofitScalarsClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)


}