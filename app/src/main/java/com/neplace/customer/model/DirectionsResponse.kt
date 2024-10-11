package com.neplace.customer.model

data class DirectionsResponse(
    val code: String,
    val routes: List<Route>,
    val uuid: String,
    val waypoints: List<Waypoint>
)

data class Route(
    val distance: Double,
    val duration: Double,
    val geometry: Geometry,
    val legs: List<Leg>,
    val weight: Double,
    val weight_name: String
)

data class Waypoint(
    val distance: Double,
    val location: List<Double>,
    val name: String
)

data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)

data class Leg(
    val admins: List<Admin>,
    val distance: Double,
    val duration: Double,
    val steps: List<Step>,
    val summary: String,
    val via_waypoints: List<Any>,
    val weight: Double
)

data class Admin(
    val iso_3166_1: String,
    val iso_3166_1_alpha3: String
)

data class Step(
    val distance: Double,
    val driving_side: String,
    val duration: Double,
    val geometry: Geometry,
    val intersections: List<Intersection>,
    val maneuver: Maneuver,
    val mode: String,
    val name: String,
    val ref: String,
    val rotary_name: String,
    val weight: Double
)

data class Intersection(
    val admin_index: Int,
    val bearings: List<Int>,
    val duration: Double,
    val entry: List<Boolean>,
    val geometry_index: Int,
    val `in`: Int,
    val is_urban: Boolean,
    val location: List<Double>,
    val mapbox_streets_v8: MapboxStreetsV8,
    val `out`: Int,
    val turn_duration: Double,
    val turn_weight: Double,
    val weight: Double
)

data class Maneuver(
    val bearing_after: Int,
    val bearing_before: Int,
    val exit: Int,
    val instruction: String,
    val location: List<Double>,
    val modifier: String,
    val type: String
)

data class MapboxStreetsV8(
    val `class`: String
)