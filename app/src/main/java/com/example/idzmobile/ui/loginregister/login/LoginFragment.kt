package com.example.idzmobile.ui.loginregister.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.idzmobile.R
import com.example.idzmobile.databinding.LoginFragmentBinding
import com.example.idzmobile.helper.Const
import com.example.idzmobile.helper.EmptyTextWatcher
import com.example.idzmobile.helper.Util
import com.example.idzmobile.ui.home.HomeActivity
import com.example.idzmobile.ui.loginregister.LoginRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    
    companion object {
        fun newInstance() = LoginFragment()
    }
    
    private val viewModel: LoginViewModel by viewModels()
    private val loginregisterViewModel: LoginRegisterViewModel by activityViewModels()
    private lateinit var binding: LoginFragmentBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        with(viewModel){
            isLoading.observe(requireActivity()) {
                loginregisterViewModel.setLoading(it)
            }
            
            response.observe(requireActivity()){
                if(it != null){
                    if(it.status.equals(Const.STATUS_FAILED)){
                        Util.showMessage(requireActivity(), it.error!!, ContextCompat.getColor(requireContext(), R.color.red))
                    } else if (it.status.equals(Const.STATUS_SUCCESS)){
                        initToken(it.token!!, it.username!!)
                        requireContext().startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    }
                }
            }
            
            isConnected.observe(requireActivity()){
                if(!it){
                    Util.showMessage(requireActivity(), getString(R.string.alert_connection), ContextCompat.getColor(requireContext(), R.color.red))
                }
            }
        }
        
        with(binding){
            tvNoAccount.setOnClickListener { v -> loginregisterViewModel.changeFragment(Const.CONST_FRAGMENT_REGISTER) }
            edtUsername.addTextChangedListener(EmptyTextWatcher(binding.layoutUsername, resources.getString(R.string.empty_message, "Username")))
            edtPassword.addTextChangedListener(EmptyTextWatcher(binding.layoutPassword, resources.getString(R.string.empty_message, "Password")))
    
            btnLogin.setOnClickListener {
                var error = false
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                
                if(username.isEmpty()){
                    error = true
                    layoutUsername.error = resources.getString(R.string.empty_message, "Username")
                }
    
                if(password.isEmpty()){
                    error = true
                    layoutPassword.error = resources.getString(R.string.empty_message, "Password")
                }
                
                if(error){
                    Util.showMessage(requireActivity(), resources.getString(R.string.check), ContextCompat.getColor(requireContext(), R.color.red))
                } else {
                    viewModel.login(username, password)
                }
            }
        }
       
    }
    
    private fun initToken(token: String, username: String) {
        val sharedPreferences = requireContext().getSharedPreferences(Const.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        
        editor.putString(Const.KEY_TOKEN, token)
        editor.putString(Const.KEY_USERNAME, username)
        editor.apply()
    }
    
}