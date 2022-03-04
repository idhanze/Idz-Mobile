package com.example.idzmobile.ui.transfer

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.idzmobile.R
import com.example.idzmobile.data.model.Payees
import com.example.idzmobile.data.model.TransferResponse
import com.example.idzmobile.databinding.ActivityTransferBinding
import com.example.idzmobile.databinding.DialogSuccessBinding
import com.example.idzmobile.helper.Const
import com.example.idzmobile.helper.EmptyTextWatcher
import com.example.idzmobile.helper.Util
import com.example.idzmobile.ui.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTransferBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listPayees: List<Payees>
    private val viewModel: TransferViewModel by viewModels()
    private var selectedIndex = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Util.initLayout(this, binding.root)
        
        with(binding){
            tvPayees.addTextChangedListener(EmptyTextWatcher(layoutPayees, getString(R.string.empty_message, "Payees")))
            edtAmount.addTextChangedListener(EmptyTextWatcher(layoutAmount, getString(R.string.empty_message, "Amount")))
            edtAmount.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if(!hasFocus){
                    val textDouble = edtAmount.text.toString().toDouble()
                    edtAmount.setText(Util.StringFormatCurrency(textDouble))
                }
            }
    
            refreshLayout.setOnRefreshListener {
                refreshLayout.isRefreshing = false
                viewModel.getPayees()
            }
    
            (binding.layoutPayees.editText as AutoCompleteTextView).onItemClickListener =
                OnItemClickListener { _, _, position, _ -> selectedIndex = position }
            
            btnTransfer.setOnClickListener {
                val payees = listPayees[selectedIndex].accountNo
                val amount = edtAmount.text.toString().replace(",", "")
                val description = edtDesc.text.toString()
                
                var error = false
                
                if(payees!!.isEmpty()){
                    layoutPayees.error = getString(R.string.empty_message, "Payees")
                    error = true
                }
                
                if(amount.isEmpty()){
                    layoutAmount.error = getString(R.string.empty_message, "Payees")
                    error = true
                }
                
                if(error){
                    Util.showMessage(this@TransferActivity, "Please check your input", ContextCompat.getColor(this@TransferActivity, R.color.red))
                } else {
                    viewModel.transfer(payees, amount.toDouble(), description)
                }
                
            }
        }
        
        val loadingDialog = LoadingDialog(this)
        
        with(viewModel){
            isLoading.observe(this@TransferActivity){
                if(it){
                    loadingDialog.show()
                } else {
                    loadingDialog.dismiss()
                }
            }
    
            isConnected.observe(this@TransferActivity){
                if(!it){
                    Util.showMessage(this@TransferActivity, getString(R.string.alert_connection), ContextCompat.getColor(this@TransferActivity, R.color.red))
                }
            }
            
            payeesResponse.observe(this@TransferActivity){ response ->
                if(response != null){
                    if(response.status == Const.STATUS_SUCCESS){
                        listPayees = response.payees as List<Payees>
                        val listName = response.payees.map { it -> it.name }
                        val adapter = ArrayAdapter<String?>(this@TransferActivity, R.layout.item_dropdown_payees, listName)
                        binding.tvPayees.setAdapter(adapter)
                    } else {
                        Util.showMessage(this@TransferActivity, "Failed to load data", ContextCompat.getColor(this@TransferActivity, R.color.red))
                    }
                }
            }
            
            transferResponse.observe(this@TransferActivity){ response ->
                if(response != null){
                    if(response.status == Const.STATUS_SUCCESS){
                        showDialog(response)
                    } else {
                        Util.showMessage(this@TransferActivity, response.error!!, ContextCompat.getColor(this@TransferActivity, R.color.red))
                    }
                }
            }
        }
    }
    
    private fun showDialog(response: TransferResponse){
        val dialog = Dialog(this)
        val dialogBinding = DialogSuccessBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        
        with(dialogBinding){
            amount.text = Util.StringFormatCurrency(response.amount!!)
            transactionId.text = getString(R.string.transaction_id, response.transactionId)
            recipient.text = getString(R.string.receipient, response.recipientAccount)
            
            btnOk.setOnClickListener {
                dialog.dismiss()
                finish()
            }
        }
        dialog.show()
    }
}