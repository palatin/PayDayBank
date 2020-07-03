package com.payday.paydaybank.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.payday.paydaybank.MainActivity
import com.payday.paydaybank.R
import com.payday.paydaybank.util.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val vm by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login.setOnClickListener {
            tl_email.error = null
            tl_password.error = null
            vm.processIntent(LoginViewModel.Intent.SignInClicked(tl_email.editText!!.text.toString(), tl_password.editText!!.text.toString()))
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            vm.actionFlow.collect {
                when(it) {
                    is LoginViewModel.Action.OpenDashboard -> {
                        (requireActivity() as MainActivity).account = it.account
                        findNavController().navigateSafe(R.id.action_loginFragment_to_nav_dashboard)
                    }
                    LoginViewModel.Action.ShowInvalidEmailValidation -> tl_email.error = "Invalid email"
                    LoginViewModel.Action.ShowInvalidPasswordValidation -> tl_password.error = "Password should be at least 6 length alphanumeric"
                }
            }
        }
        vm.processIntent(LoginViewModel.Intent.TrySignIn)
    }


}