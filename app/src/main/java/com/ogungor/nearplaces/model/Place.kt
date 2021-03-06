package com.ogungor.nearplaces.model

import Geometry

class Place {
    var id: String? = null
    var place_id: String? = null
    var price_level: Int = 0
    var rating: Double = 0.0
    var reference: String? = null
    var scope: String? = null
    var types: Array<String>? = null
    var vicinity: String? = null
    var name: String? = null
    var icon: String? = null
    var geometry: Geometry? = null
}

