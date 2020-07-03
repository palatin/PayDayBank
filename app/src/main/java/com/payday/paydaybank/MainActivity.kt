package com.payday.paydaybank

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.payday.paydaybank.model.Account
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var account: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null && savedInstanceState.containsKey("account")) {
            account = savedInstanceState.getParcelable("account")
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(account != null)
            outState.putParcelable("account", account)
    }

    fun provideCurrentFragment(): Fragment {
        return root.childFragmentManager.fragments.first()
    }
}