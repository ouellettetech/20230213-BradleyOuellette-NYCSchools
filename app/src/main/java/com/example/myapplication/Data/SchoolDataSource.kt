package com.example.myapplication.Data

import java.util.HashMap
import kotlin.reflect.KFunction0
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

object SchoolDataSource {

    /**
     * An array of School Items
     */
    val ITEMS: MutableList<School> = ArrayList()

    /**
     * A map of school SAT Info by database ID
     */
    val ITEM_MAP: MutableMap<String, SchoolSATInfo> = HashMap()

    var dataUpdateCallback: MutableList<KFunction0<Unit>> = ArrayList();

    init {
        updateData()
    }

    private fun convertJSONtoSchool(json: String){
        var gson = Gson()
        val arraySchoolType = object : TypeToken<Array<School>>() {}.type
        var schools: Array<School> = gson.fromJson(json, arraySchoolType)

        if(schools.size > 0 ){
            ITEMS.clear()
            ITEMS.addAll(schools.asList())
            updateDataUsage();
        }
    }

    private fun updateDataUsage(){

        dataUpdateCallback.forEach {
            it()
        }
    }

    public fun setDataChangeListener( callback: KFunction0<Unit>){
        dataUpdateCallback.add(callback);
    }

    public fun removeDataChangeListener(callback: KFunction0<Unit>){
        dataUpdateCallback.remove(callback);
    }

    public fun updateData(){
        NetworkConnection.getSchoolListJSON( ::convertJSONtoSchool)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }
}