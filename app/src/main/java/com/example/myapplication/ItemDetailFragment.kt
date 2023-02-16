package com.example.myapplication

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.Data.SchoolDataSource
import com.example.myapplication.Data.SchoolSATDataSource
import com.example.myapplication.Data.SchoolSATInfo
import com.example.myapplication.databinding.FragmentItemDetailBinding

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
    var DBN: String? = null

    lateinit var numStudentsTextView: TextView
    lateinit var readingScoreTextView: TextView
    lateinit var writingScoreTextView: TextView
    lateinit var mathScoreTextView: TextView
    lateinit var schoolNameTextView : TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null

    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            DBN = dragData.toString()
            updateSATValue()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_SCHOOL_DBN)) {
                DBN = it.getString(ARG_SCHOOL_DBN)
                updateSATValue()
            }
        }
        SchoolSATDataSource.setDataChangeListener(::updateDataAdapter)
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
        schoolNameTextView = binding.titleSchoolName

        //updateContent() // Might be nice to have the page load wait for all the data to come down before loading, I've done spinners in while getting it as one option.
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        toolbarLayout?.title = ""
        schoolNameTextView.text = item?.school_name ?: "Unknown School"

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

    private fun updateDataAdapter(dbn: String){
        if(dbn.equals(DBN)){
            this@ItemDetailFragment.activity?.runOnUiThread(java.lang.Runnable { // Since this updated the UI we need to switch back to the UI thread.
                DBN?.let {
                    item = SchoolSATDataSource.getSchoolSAT(it)
                    updateContent()
                }
            })
        }
    }

    private fun updateSATValue(){
        if(DBN == null){
            return
        }
        DBN?.let {
            val satResult = SchoolSATDataSource.getSchoolSAT(it)
            item = satResult
        }
    }


    // probably should add save the current state not sure I'm going to get to because of time.

    override fun onDestroyView() {
        super.onDestroyView()
        SchoolSATDataSource.removeDataChangeListener(::updateDataAdapter)
        _binding = null
    }
}