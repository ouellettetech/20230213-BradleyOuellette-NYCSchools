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
     * A map of school by database ID
     */
    val ITEM_MAP: MutableMap<String, School> = HashMap()

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

            ITEM_MAP.clear()
            ITEMS.forEach{
                it.dbn?.let { dbn -> ITEM_MAP[dbn] = it };
            }
            updateDataUsage()
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
}