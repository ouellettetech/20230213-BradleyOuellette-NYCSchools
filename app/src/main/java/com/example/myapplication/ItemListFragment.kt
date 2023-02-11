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
import com.example.myapplication.placeholder.PlaceholderContent;
import com.example.myapplication.databinding.FragmentItemListBinding
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import kotlin.concurrent.thread

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class ItemListFragment : Fragment() {

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

        val recyclerView: RecyclerView = binding.itemList

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        setupRecyclerView(recyclerView, itemDetailFragmentContainer)
    }

    private fun sendGet() {
        Thread(Runnable {
            val url = URL("https://data.cityofnewyork.us/resource/s3k6-pzi2.json?\$select=school_name,dbn")

            val json = try {
                url.readText()
            } catch (e: Exception) {
                "[]"
            }
            println(json)
//            with(url.openConnection() as HttpURLConnection) {
//                requestMethod = "GET"  // optional default is GET
//
//
//                inputStream.bufferedReader().use {
//                    it.lines().forEach { line ->
//                        println(line)
//                    }
//                }
//            }


        }).start()
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        itemDetailFragmentContainer: View?
    ) {
        println("BRAD NEW World 2")
        sendGet()

//        thread {
//            val json = try {
//                URL("https://data.cityofnewyork.us/resource/s3k6-pzi2.json").readText()
//            } catch (e: Exception) {
//                return@thread
//            }
//            println("BRAD Printing results.")
//            println(json)
//            println("BRAD Printed Results")
//            //runOnUiThread { println(json) }
//        }

//        val client = HttpClient.newBuilder().build();
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create("https://something.com"))
//            .build();
//        val response = client.send(request, BodyHandlers.ofString());
//        println(response.body())


        println("BRAD Hello World")
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            PlaceholderContent.ITEMS, itemDetailFragmentContainer
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}