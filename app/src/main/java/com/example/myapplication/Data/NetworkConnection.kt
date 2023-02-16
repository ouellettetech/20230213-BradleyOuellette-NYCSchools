package com.example.myapplication.Data

import android.os.Handler
import java.net.URL
import kotlin.reflect.KFunction1

class NetworkConnection {
    companion object {
        private val schoolsUrl: String = "https://data.cityofnewyork.us/resource/s3k6-pzi2.json?\$select=school_name,dbn";
        private val satValues: String = "https://data.cityofnewyork.us/resource/f9bf-2cp4.json?dbn=";

        public fun getSchoolListJSON(callback: KFunction1<String, Unit>) {
            getJSON(schoolsUrl,callback);
        }

        public fun getSATValuesJSON(schoolName: String, callback: KFunction1<String, Unit>){
            getJSON(satValues+schoolName,callback)
        }

        fun getJSON(url: String, callback: KFunction1<String, Unit>) {
            getJSONHelper(url, callback,0)
        }

        private fun getJSONHelper(urlString: String, callback: KFunction1<String, Unit>, retryCount: Int){
            Thread(Runnable {
                val url = URL(urlString)

                try {
                    val json=url.readText()
                    callback(json)
                } catch (e: Exception) {
                    if(retryCount<5) { // We want to retry the request if it fails. Given more time I would catch the different Exceptions and decide which to retry.
                        // Given more time I also would have put a pull down to refresh if this is data that can change.
                        Handler().postDelayed({
                            getJSONHelper(urlString, callback, retryCount)
                        }, 5000)
                    }
                }
            }).start()
        }
    }
}