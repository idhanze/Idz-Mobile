package com.example.idzmobile.ui.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.idzmobile.R
import com.example.idzmobile.data.model.BalanceResponse
import com.example.idzmobile.data.model.DataItemTransaction
import com.example.idzmobile.data.model.Section
import com.example.idzmobile.data.model.TransactionResponse
import com.example.idzmobile.databinding.ActivityHomeBinding
import com.example.idzmobile.helper.Const
import com.example.idzmobile.helper.Util
import com.example.idzmobile.ui.LoadingDialog
import com.example.idzmobile.ui.loginregister.LoginRegisterActivity
import com.example.idzmobile.ui.transfer.TransferActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private var listSection = arrayListOf<Section>()
    private lateinit var listTransaction: ArrayList<DataItemTransaction>
    
    private val TIME_INTERVAL = 2000
    
    private var mBackPressed: Long = 0
    
    
    val viewModel: HomeViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Util.initLayout(this, binding.root)
        
        initTransaction()
    
        val dialog = LoadingDialog(this)
        
        with(binding){
            refreshLayout.setOnRefreshListener {
                refreshLayout.isRefreshing = false
                viewModel.getData()
            }
    
            scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                refreshLayout.isEnabled = scrollY == 0
            }
            
            btnTransfer.setOnClickListener{v-> startActivity(Intent(this@HomeActivity, TransferActivity::class.java))}
            
            btnLogout.setOnClickListener {
                val builder = AlertDialog.Builder(this@HomeActivity)
                builder.apply {
                    setMessage("Are you sure you want to exit?")
                    setPositiveButton("Yes") { dialog, id ->
                        startActivity(Intent(this@HomeActivity, LoginRegisterActivity::class.java))
                        finish()
                    }
                    setNegativeButton("No"){ dialog, id ->
                        dialog.dismiss()
                    }
                    show()
                }
            }
        }
        
        with(viewModel){
            isConnected.observe(this@HomeActivity){
                if(!it){
                    Util.showMessage(this@HomeActivity, getString(R.string.alert_connection), ContextCompat.getColor(this@HomeActivity, R.color.red))
                }
            }
    
            isLoading.observe(this@HomeActivity){
                if(it)
                    dialog.show()
                else
                    dialog.dismiss()
            }
    
            transactionResponse.observe(this@HomeActivity){ response ->
                if(response != null){
                    if(response.status.equals(Const.STATUS_SUCCESS)){
                        listSection.clear()
                        if(response.data == null || response.data.isEmpty()){
                            binding.txtEmpty.visibility = View.VISIBLE
                        } else {
                            initTransactionData(response)
                            for (i in 0..2){
                                Handler().postDelayed({
                                    Timber.d(transactionAdapter.itemCount.toString())
                                }, 3000)
                            }
                        }
                    } else {
                        Util.showMessage(this@HomeActivity, "Failed to load data", ContextCompat.getColor(this@HomeActivity, R.color.red))
                    }
                }
            }
    
            balanceResponse.observe(this@HomeActivity){ response ->
                if(response != null){
                    if(response.status.equals(Const.STATUS_SUCCESS)){
                        initBalanceData(response)
                    } else {
                        Util.showMessage(this@HomeActivity, "Gagal mendapatkan data", ContextCompat.getColor(this@HomeActivity, R.color.red))
                    }
                }
        
            }
        }
        
    }
    
    private fun initBalanceData(response: BalanceResponse){
        val preferences = getSharedPreferences(Const.SHARED_PREF_NAME, MODE_PRIVATE)
        binding.tvWelcome.text = getString(R.string.welcome_message, preferences.getString(Const.KEY_USERNAME, ""))
        binding.tvCardNumber.text = response.accountNo
        binding.tvBalance.text = Util.StringFormatCurrency(response.balance!!)
    }
    
    @SuppressLint("NotifyDataSetChanged")
    private fun initTransactionData(response: TransactionResponse) {
        response.data!!.sortedByDescending { it?.transactionDate }
    
        var sectionTitle = ""
        listTransaction = arrayListOf()
        for((index, value) in response.data.withIndex()){
            if(index == 0){
                sectionTitle = formatDate(value!!.transactionDate)
                listTransaction.add(value)
            } else {
                val currentTitle = formatDate(value!!.transactionDate)

                if(index < response.data.size - 1){
                    if(currentTitle != sectionTitle){
                        val section = Section(sectionTitle, listTransaction)
                        listSection.add(section)
                        sectionTitle = currentTitle
                        listTransaction = arrayListOf()
                    }

                    listTransaction.add(value)
                } else {
                    if(currentTitle != sectionTitle){
                        listTransaction = arrayListOf()
                    }

                    listTransaction.add(value)

                    val section = Section(sectionTitle, listTransaction)
                    listSection.add(section)
                }
            }
        }
    
        transactionAdapter.notifyDataSetChanged()
    }
    
    private fun initTransaction() {
        transactionAdapter = TransactionAdapter(listSection)
        with(binding){
            rvTransaction.adapter = transactionAdapter
            rvTransaction.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
        }
    }
    
    @SuppressLint("SimpleDateFormat")
    private fun formatDate(d: String?): String{
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        originalFormat.isLenient = false
        val targetFormat: DateFormat = SimpleDateFormat("dd MMM yyyy");
        val date = originalFormat.parse(d!!.replace("Z$", "+0000")) as Date
        return targetFormat.format(date) as String
    }
    
    override fun onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Util.showMessage(this, getString(R.string.alert_exit), ContextCompat.getColor(this, R.color.purple))
        }
        mBackPressed = System.currentTimeMillis()
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }
}