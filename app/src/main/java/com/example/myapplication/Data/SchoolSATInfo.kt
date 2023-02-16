package com.example.myapplication.Data

data class SchoolSATInfo (
    var dbn: String? = null,
    var num_of_sat_test_takers: Int = 0,// The names match the API, we could change them to be more friendly, but then I would need to customize the JSON parser,
    // In a real application it would be a tradeoff between the time/effort/maintenance of a custom json parser vs cost of changing the API.
    var sat_critical_reading_avg_score: Int = 0,
    var sat_math_avg_score: Int = 0,
    var sat_writing_avg_score: Int = 0,
    var school_name: String? = null){
    override fun toString(): String = school_name ?: "Unknown School"
}