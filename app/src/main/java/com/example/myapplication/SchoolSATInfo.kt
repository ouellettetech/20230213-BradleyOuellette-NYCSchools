package com.example.myapplication

data class SchoolSATInfo (
    var dbn: String? = null,
    var num_of_sat_test_takers: Int = 0,
    var sat_critical_reading_avg_score: Int = 0,
    var sat_math_avg_score: Int = 0,
    var sat_writing_avg_score: Int = 0,
    var school_name: String? = null){
    override fun toString(): String = school_name ?: "Unknown School"
}