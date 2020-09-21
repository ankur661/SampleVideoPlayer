package com.example.samplevideoplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.samplevideoplayer.R
import com.example.samplevideoplayer.listeners.FragmentVideoClickListener

class MainActivity : AppCompatActivity(), FragmentVideoClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.main_activity)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    MainFragment.newInstance()
                )
                .commit()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is MainFragment) {
            fragment.setOnVideoClickedListener(this)
        }
    }

    override fun onVideoClicked() {
        launchVideoFragment()
    }

    private fun launchVideoFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                VideoFragment.newInstance()
            )
            .addToBackStack("VideoView")
            .commit()
    }

    override fun onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed()
        }
    }
}
