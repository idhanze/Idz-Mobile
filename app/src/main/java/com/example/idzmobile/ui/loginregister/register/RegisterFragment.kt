package com.example.idzmobile.ui.loginregister.register

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
import com.example.idzmobile.databinding.RegisterFragmentBinding
import com.example.idzmobile.helper.Const
import com.example.idzmobile.helper.EmptyTextWatcher
import com.example.idzmobile.helper.Util
import com.example.idzmobile.ui.home.HomeActivity
import com.example.idzmobile.ui.loginregister.LoginRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    
    companion object {
        fun newInstance() = RegisterFragment()
    }
    
    private val viewModel: RegisterViewModel by viewModels()
    private val loginRegisterViewModel: LoginRegisterViewModel by activityViewModels()
    private lateinit var binding: RegisterFragmentBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegisterFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        with(binding){
            tvHaveAccount.setOnClickListener { v -> loginRegisterViewModel.changeFragment(Const.CONST_FRAGMENT_LOGIN) }
            edtUsername.addTextChangedListener(EmptyTextWatcher(layoutUsername, getString(R.string.empty_message, "Username")))
            edtPassword.addTextChangedListener(EmptyTextWatcher(layoutPassword, getString(R.string.empty_message, "Password")))
            edtConfirmPassword.addTextChangedListener(EmptyTextWatcher(layoutConfirmPassword, getString(R.string.empty_message, "Confirm Password")))
            
            btnRegister.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtUsername.text.toString()
                val confirmPassword = edtUsername.text.toString()
                
                var error = false
                
                if(username.isEmpty()){
                    error = true
                    layoutUsername.error = getString(R.string.empty_message, "Username")
    
                }
                
                if(password.isEmpty()){
                    error = true
                    layoutPassword.error = getString(R.string.empty_message, "Password")
                }
                
                if(confirmPassword.isEmpty()){
                    error = true
                    layoutConfirmPassword.error = getString(R.string.empty_message, "Confirm Password")
                } else if(confirmPassword != password){
                    error = true
                    layoutConfirmPassword.error = "Password not match"
                }
                
                if(error){
                    Util.showMessage(requireActivity(), getString(R.string.check), ContextCompat.getColor(requireContext(), R.color.red))
                } else {
                    viewModel.register(username, password)
                }
            }
            
        }
    
        with(viewModel){
            isLoading.observe(requireActivity()) {
                loginRegisterViewModel.setLoading(it)
            }
        
            response.observe(requireActivity()){
                if(it != null){
                    if(it.status.equals(Const.STATUS_FAILED)){
                        Util.showMessage(requireActivity(), it.error!!, ContextCompat.getColor(requireContext(), R.color.red))
                    } else if (it.status.equals(Const.STATUS_SUCCESS)){
                        Util.showMessage(requireActivity(), "Success, you can login now", ContextCompat.getColor(requireContext(), R.color.red))
                        loginRegisterViewModel.changeFragment(Const.CONST_FRAGMENT_LOGIN)
                    }
                }
            }
        
            isConnected.observe(requireActivity()){
                if(!it){
                    Util.showMessage(requireActivity(), getString(R.string.alert_connection), ContextCompat.getColor(requireContext(), R.color.red))
                }
            }
        }
    }
    
    
}