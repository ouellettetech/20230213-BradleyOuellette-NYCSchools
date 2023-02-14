package com.example.myapplication.Data

data class School ( // School Details
    var dbn: String? = null, // The names match the API, we could change them to be more friendly, but then I would need to customize the JSON parser,
    // In a real application it would be a tradeoff between the time/effort/maintenance of a custom json parser vs cost of changing the API.
    var school_name: String? = null){
    override fun toString(): String = school_name ?: "Unknown School"
}