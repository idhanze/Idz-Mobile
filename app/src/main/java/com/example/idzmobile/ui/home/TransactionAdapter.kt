package com.example.idzmobile.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.idzmobile.data.model.Section
import com.example.idzmobile.databinding.ItemTransactionBinding
import timber.log.Timber

class TransactionAdapter(private val listSection: List<Section>): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listSection[position]
        val adapter = SectionAdapter(data.sectionList!!)
        Timber.d(data.sectionTitle)
        with(holder.binding){
            txtDate.text = data.sectionTitle
            rvItem.adapter = adapter
            rvItem.layoutManager = LinearLayoutManager(holder.itemView.context)
            rvItem.hasFixedSize()
        }
    }
    
    override fun getItemCount(): Int = listSection.size
    
    class ViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
    
    }
    
    
}