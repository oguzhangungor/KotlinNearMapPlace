package com.ogungor.nearplaces.util.urlprocess

class UrlProvider {

    fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latitude,$longitude&radius=300&type=$typePlace&key=AIzaSyBLWNF4ChIdv8co_K8vtXsd3d3_z6DDrLc"
    }
}