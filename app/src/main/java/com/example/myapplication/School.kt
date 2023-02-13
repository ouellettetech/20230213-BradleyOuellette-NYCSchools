package com.example.myapplication

data class School (
    var id: String = "1",
    var dbn: String? = null,
    var school_name: String? = null){
    override fun toString(): String = school_name ?: "Unknown School"
}