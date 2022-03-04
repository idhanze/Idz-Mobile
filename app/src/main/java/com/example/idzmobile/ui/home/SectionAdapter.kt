package com.example.idzmobile.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.idzmobile.data.model.DataItemTransaction
import com.example.idzmobile.databinding.ItemSectionBinding
import com.example.idzmobile.databinding.ItemTransactionBinding
import com.example.idzmobile.helper.Util
import timber.log.Timber

class SectionAdapter(private val listTransaction: List<DataItemTransaction>): RecyclerView.Adapter<SectionAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listTransaction[position]
        holder.bindView(data)
        Timber.d(data.receipient.toString())
    }
    
    override fun getItemCount(): Int = listTransaction.size
    
    class ViewHolder(private val binding: ItemSectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: DataItemTransaction){
            with(binding){
                tvName.text = data.receipient!!.accountHolder
                tvAmount.text = Util.StringFormatCurrency(data.amount!!)
                tvCardNumber.text = data.receipient.accountNo
            }
        }
    }
}