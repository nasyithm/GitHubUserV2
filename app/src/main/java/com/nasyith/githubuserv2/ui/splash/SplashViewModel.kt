package com.nasyith.githubuserv2.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nasyith.githubuserv2.data.UserRepository

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> = userRepository.getThemeSetting()
}