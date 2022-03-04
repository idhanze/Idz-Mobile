package com.example.idzmobile.ui.loginregister

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
import com.example.idzmobile.R
import com.example.idzmobile.databinding.ActivityLoginRegisterBinding
import com.example.idzmobile.helper.Const
import com.example.idzmobile.helper.Util
import com.example.idzmobile.ui.LoadingDialog
import com.example.idzmobile.ui.home.HomeActivity
import com.example.idzmobile.ui.loginregister.login.LoginFragment
import com.example.idzmobile.ui.loginregister.register.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private val viewModel: LoginRegisterViewModel by viewModels()
    
    private val TIME_INTERVAL = 2000
    
    private var mBackPressed: Long = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setOnExitAnimationListener { splashScreen ->
            val animator = ObjectAnimator.ofFloat(
                splashScreen.view,
                View.ALPHA,
                1f,
                0f
            )
            animator.duration = 1000L
            animator.doOnEnd {
                splashScreen.remove()
            }
            animator.start()
        }
    
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Util.initLayout(this, binding.root)
    
        val loadingDialog = LoadingDialog(this)
    
        viewModel.isLoading.observe(this) {
            if (it)
                loadingDialog.show()
            else
                loadingDialog.dismiss()
        }
    
        viewModel.fragmentNumber.observe(this){
            if (it == Const.CONST_FRAGMENT_REGISTER)
                loadFragmentRegister()
            else if(it == Const.CONST_FRAGMENT_LOGIN)
                loadFragmentLogin()
        }
    
        loadFragmentLogin()
    }
    
    
    fun loadFragmentLogin(){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(R.id.container, LoginFragment())
            .addToBackStack(null)
            .commit()
    }
    
    fun loadFragmentRegister(){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            .replace(R.id.container, RegisterFragment())
            .addToBackStack(null)
            .commit()
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
}