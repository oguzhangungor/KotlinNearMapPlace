package com.ogungor.kotlinnearmapplace.util.markerprocess

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ogungor.kotlinnearmapplace.Model.Place
import com.ogungor.kotlinnearmapplace.R
import com.ogungor.kotlinnearmapplace.enum.PlaceType
import com.ogungor.kotlinnearmapplace.enum.PlaceType.*

class MarkerProvider {


    fun getRelatedMarker(place: Place, placeType: PlaceType): MarkerOptions {

        val lat = place.geometry?.location?.lat!!
        val lng = place.geometry?.location?.lng!!
        val name = place.name!!
        val locationPosition = LatLng(lat, lng)
        return MarkerOptions().apply {
            position(locationPosition)
            title(name)
            icon(getRelatedMarkerIcon(placeType))
            snippet(placeType.toString())
        }

    }

    private fun getRelatedMarkerIcon(placeType: PlaceType): BitmapDescriptor {
        return when (placeType) {
            MARKET -> BitmapDescriptorFactory.fromResource(R.drawable.ic_shopping)
            HOSPITAL -> BitmapDescriptorFactory.fromResource(R.drawable.ic_hospital)
            SCHOOL -> BitmapDescriptorFactory.fromResource(R.drawable.ic_school)
            RESTAURANT -> BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant)
        }


    }
}