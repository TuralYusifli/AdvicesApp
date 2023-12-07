package com.example.advicesapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericAdapter<T, VB : ViewBinding, VH : RecyclerView.ViewHolder>(
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val viewHolderFactory: (VB) -> VH,
    itemCallback: DiffUtil.ItemCallback<T>,
    private val bindViewHolder: (VH, T) -> Unit,
    private val clickListeners: List<Pair<Int, (T) -> Unit>>

) : ListAdapter<T, VH>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = bindingInflater.invoke(inflater, parent, false)
        val viewHolder = createViewHolder(binding)
        for ((viewId, clickListener) in clickListeners) {
            viewHolder.itemView.findViewById<View>(viewId)?.setOnClickListener {
                val item = getItem(viewHolder.adapterPosition)
                clickListener(item)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        bindViewHolder(holder, item)
    }

    private fun createViewHolder(binding: VB): VH {
        return viewHolderFactory(binding)
    }

}