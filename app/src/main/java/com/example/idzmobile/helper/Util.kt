package com.example.idzmobile.helper

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat


class Util{
    companion object{
        fun showMessage(activity: Activity, message: String, color: Int){
            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(color).show()
        }
    
        fun StringFormatCurrency(value: Double): String? {
            val df = DecimalFormat("###,##0.00")
            return df.format(value)
        }
        
        fun initLayout(activity: Activity, v: View){
            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            ViewCompat.setOnApplyWindowInsetsListener(v) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                val mlp = view.layoutParams as ViewGroup.MarginLayoutParams
                mlp.apply {
                    leftMargin = insets.left
                    rightMargin = insets.right
                    bottomMargin = insets.bottom
                }
                WindowInsetsCompat.CONSUMED
            }
        }
    
        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        }
    }
    
}