package com.example.myapplication.Data

import java.util.HashMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList
import kotlin.reflect.KFunction1

object SchoolSATDataSource {

    /**
     * A map of school SAT Info by database ID
     */
    val ITEM_MAP: MutableMap<String, SchoolSATInfo> = HashMap() //storing already
        // retrieved data, should add a timestamp to ignore if too old.

    var dataUpdateCallback: MutableList<KFunction1<String,Unit>> = ArrayList();

    init {
    }

    private fun convertJSONtoSATFunction(json: String) {
            var gson = Gson()
            val arraySchoolType = object : TypeToken<Array<SchoolSATInfo>>() {}.type
            var satResults: Array<SchoolSATInfo> = gson.fromJson(json, arraySchoolType)
            if(satResults.size == 1 ){
                val satResult = satResults[0]
                satResult.dbn?.let {
                    ITEM_MAP[it] = satResult;
                    updateDataUsage(it)
                }
            } else {
                if(satResults.size>1){
                    //we got too many results, this means an issue with the request or the server.
                    // Log this to the server. We may want to show the first result, or just an error and no result depending on the data.
                }
                if(satResults.size == 0){
                    // We didn't get any results Show just the school name. and School Details. If there is no data I'm displaying 0 for number of SAT's and avg scores of 0.
                }
            }
    }

    public fun getSchoolSAT(dbn: String): SchoolSATInfo {
        ITEM_MAP[dbn]?.let { return it }

        NetworkConnection.getSATValuesJSON(dbn,
            ::convertJSONtoSATFunction
        )
        if(ITEM_MAP[dbn] == null){
            println("Null Value in Item Map: "+dbn)
        }
        return ITEM_MAP[dbn] ?: SchoolSATInfo(dbn,0,0,0,0, SchoolDataSource.ITEM_MAP[dbn]?.school_name)
    }

    private fun updateDataUsage(dbn: String){

        dataUpdateCallback.forEach {
            it(dbn)
        }
    }

    public fun setDataChangeListener( callback: KFunction1<String, Unit>){
        dataUpdateCallback.add(callback);
    }

    public fun removeDataChangeListener(callback: KFunction1<String, Unit>){
        dataUpdateCallback.remove(callback);
    }
}