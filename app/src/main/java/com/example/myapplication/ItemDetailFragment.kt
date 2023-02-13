package com.example.myapplication

import android.content.ClipData
import android.os.Bundle
import android.os.Handler
import android.view.DragEvent
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.databinding.FragmentItemDetailBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: SchoolSATInfo? = null

    lateinit var numStudentsTextView: TextView
    lateinit var readingScoreTextView: TextView
    lateinit var writingScoreTextView: TextView
    lateinit var mathScoreTextView: TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null

    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            getSATValue(dragData.toString(),null)
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_SCHOOL_DBN)) {
                var schoolName: String? = null
                if (it.containsKey(ARG_SCHOOL_NAME)) {
                    schoolName = it.getString(ARG_SCHOOL_NAME)
                }
                getSATValue(it.getString(ARG_SCHOOL_DBN), schoolName)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        this.activity?.title = "Hello"
        toolbarLayout = binding.toolbarLayout

        numStudentsTextView = binding.satTestTakers
        readingScoreTextView = binding.satAverageReading
        writingScoreTextView = binding.satAverageWriting
        mathScoreTextView = binding.satAverageMath

        //updateContent() // Might be nice to have the page load wait for all the data to come down before loading, I've done spinners in while getting it as one option.
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        toolbarLayout?.title = item?.school_name // Would probably rather have done this in the Relative Layout,
        // but went with the Item Detail example as starting point and too much time to rip this out.
        
        // Show the placeholder content as text in a TextView.
        item?.let {
            numStudentsTextView.text = it.num_of_sat_test_takers.toString()
            readingScoreTextView.text = it.sat_critical_reading_avg_score.toString()
            writingScoreTextView.text = it.sat_writing_avg_score.toString()
            mathScoreTextView.text = it.sat_math_avg_score.toString()
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_SCHOOL_DBN = "school_dbn"
        const val ARG_SCHOOL_NAME = "school_name"
    }

    private fun getSATValue(school_dbn: String?, school_name: String?){
        if(school_dbn != null) {
            sendGet(school_dbn, school_name)
        }
    }

    // Would normally move this into a general networking class but left here for simplicity for the example.
    private fun sendGet(school_dbn: String, school_name: String?, loop: Int = 0) {
        Thread(Runnable {
            val url = URL("https://data.cityofnewyork.us/resource/f9bf-2cp4.json?dbn=$school_dbn") // should be a const at the top of the file for the base URL, and then construct the query,

            val json = try {
                url.readText()
            } catch (e: Exception) {
                if(loop<5) { // We want to retry the request if it fails. Given more time I would catch the different Exceptions and decide which to retry.
                    // Given more time I also would have put a pull down to refresh if this is data that can change.
                    Handler().postDelayed({
                        sendGet(school_dbn, school_name, loop+1)
                    }, 5000)
                }
                "[]"
            }
            var gson = Gson()
            val arraySchoolType = object : TypeToken<Array<SchoolSATInfo>>() {}.type
            var satResults: Array<SchoolSATInfo> = gson.fromJson(json, arraySchoolType)
            var satResult: SchoolSATInfo = SchoolSATInfo()
            if(satResults.size == 1 ){
                satResult = satResults[0]
            } else {
                satResult.school_name = school_name?: "Unknown School"
                if(satResults.size>1){
                    //we got too many results, this means an issue with the request or the server.
                    // Log this to the server. We may want to show the first result, or just an error and no result depending on the data.
                }
                if(satResults.size == 0){
                    // We didn't get any results Show just the school name. and School Details. If there is no data I'm displaying 0 for number of SAT's and avg scores of 0.
                }
            }

            this@ItemDetailFragment.activity?.runOnUiThread(java.lang.Runnable { // Since this updated the UI we need to switch back to the UI thread.
                item = satResult
                updateContent()
            })
        }).start()
    }

    // probably should add save the current state not sure I'm going to get to because of time.

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}