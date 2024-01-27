package com.nasyith.githubuserv2.ui.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.nasyith.githubuserv2.databinding.ActivitySplashBinding
import com.nasyith.githubuserv2.helper.ViewModelFactory
import com.nasyith.githubuserv2.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val splashViewModel: SplashViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashIntent()
        getThemeSetting()
    }

    private fun getThemeSetting() {
        splashViewModel.getThemeSetting().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun splashIntent() {
        binding.ivSplash.alpha = 0f
        binding.ivSplash.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val anim = ActivityOptions.makeCustomAnimation(
                applicationContext,
                android.R.anim.fade_in, android.R.anim.fade_out
            ).toBundle()
            startActivity(intent, anim)
            finish()
        }
    }
}