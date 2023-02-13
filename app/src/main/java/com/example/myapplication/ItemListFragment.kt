package com.example.myapplication

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentItemListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL
import kotlin.collections.ArrayList

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class ItemListFragment : Fragment() {
    val mSchools: MutableList<School> = ArrayList()
    var mRecyclerView: RecyclerView? = null
    var mListAdapter: SimpleItemRecyclerViewAdapter? = null

    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    private val unhandledKeyEventListenerCompat =
        ViewCompat.OnUnhandledKeyEventListenerCompat { v, event ->
            if (event.keyCode == KeyEvent.KEYCODE_Z && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            } else if (event.keyCode == KeyEvent.KEYCODE_F && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            false
        }

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat)

        mRecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)
        if(mRecyclerView != null){
            setupRecyclerView(mRecyclerView!!, itemDetailFragmentContainer)
        }
    }

    private fun sendGet() { // Could retrieve this on launch earlier so its already there, but it loads really fast. Also would probably want to paginate this so we aren't loading all 400 at once.
        Thread(Runnable {
            println("BRAD Getting Schools")
            val url = URL("https://data.cityofnewyork.us/resource/s3k6-pzi2.json?\$select=school_name,dbn")

            val json = try {
                url.readText()
            } catch (e: Exception) {
                "[]"
            }
            println("BRAD Parsing Data")
            var gson = Gson()
            val arraySchoolType = object : TypeToken<Array<School>>() {}.type
            var schools: Array<School> = gson.fromJson(json, arraySchoolType)

            println("BRAD Updateing Schools")
            if(schools.size > 0 ){
                mSchools.clear()
                mSchools.addAll(schools.asList())
                // try to touch View of UI thread
                this@ItemListFragment.activity?.runOnUiThread(java.lang.Runnable {
                    println("BRAD Forcing UI Update")
                    println(" mSchool size : "+ mSchools.size)
                    mListAdapter?.updateData()
                })
            }
            println("BRAD Done Getting Data")


        }).start()
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        itemDetailFragmentContainer: View?
    ) {
        mListAdapter = SimpleItemRecyclerViewAdapter(
            mSchools, itemDetailFragmentContainer
        )

        sendGet()


        recyclerView.adapter = mListAdapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}