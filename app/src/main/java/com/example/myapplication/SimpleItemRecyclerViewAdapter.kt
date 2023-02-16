package com.example.myapplication

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Data.School
import com.example.myapplication.databinding.ItemListContentBinding

class SimpleItemRecyclerViewAdapter(
    private val values:  MutableList<School>,
    private val itemDetailFragmentContainer: View?
) :
    RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.school_name

        with(holder.itemView) {
            tag = item
            setOnClickListener { itemView ->
                val schoolItem = itemView.tag as School
                val bundle = Bundle()
                bundle.putString(
                    ItemDetailFragment.ARG_SCHOOL_DBN,
                    schoolItem.dbn
                )
                bundle.putString(
                    ItemDetailFragment.ARG_SCHOOL_NAME,
                    schoolItem.school_name
                )
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /**
                 * Context click listener to handle Right click events
                 * from mice and trackpad input to provide a more native
                 * experience on larger screen devices
                 */
                setOnContextClickListener { v ->
                    val schoolItem = v.tag as School
                    Toast.makeText(
                        v.context,
                        "Context click of item " + schoolItem.dbn,
                        Toast.LENGTH_LONG
                    ).show()
                    true
                }
            }

            setOnLongClickListener { v ->
                // Setting the item id as the clip data so that the drop target is able to
                // identify the id of the content
                val clipItem = ClipData.Item(item.dbn)
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )

                if (Build.VERSION.SDK_INT >= 24) {
                    v.startDragAndDrop(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                } else {
                    @Suppress("DEPRECATION") // Ignoring since this is a Warning about using an old feature that only happens if running on an old OS.
                    v.startDrag(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                }
            }
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
    }

    fun updateData() {
        notifyDataSetChanged()
    }
}