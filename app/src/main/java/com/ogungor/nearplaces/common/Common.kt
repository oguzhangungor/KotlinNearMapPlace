package com.ogungor.nearplaces.common

import com.ogungor.nearplaces.remote.IGoogleAPIService
import com.ogungor.nearplaces.remote.RetrofitClient

object Common {

    private const val GOOGLE_API_URL="https://maps.googleapis.com/"

    val googleAPIService:IGoogleAPIService
        get() = RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}