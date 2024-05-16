package com.outerspace.ip_challenge.network_layer

import com.squareup.moshi.Json

class IPSchema {
    @Json(name = "query")
    var query: String? = null

    @Json(name = "status")
    var status: String? = null

    @Json(name = "country")
    var country: String? = null

    @Json(name = "countryCode")
    var countryCode: String? = null

    @Json(name = "region")
    var region: String? = null

    @Json(name = "regionName")
    var regionName: String? = null

    @Json(name = "city")
    var city: String? = null

    @Json(name = "zip")
    var zip: String? = null

    @Json(name = "lat")
    var lat: Double? = null

    @Json(name = "lon")
    var lon: Double? = null

    @Json(name = "timezone")
    var timezone: String? = null

    @Json(name = "isp")
    var isp: String? = null

    @Json(name = "org")
    var org: String? = null

    @Json(name = "as")
    var autonomousSystem: String? = null

}