package com.nasyith.githubuserv2.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasyith.githubuserv2.data.UserRepository
import com.nasyith.githubuserv2.di.Injection
import com.nasyith.githubuserv2.ui.detailuser.DetailUserViewModel
import com.nasyith.githubuserv2.ui.favoriteuser.FavoriteUserViewModel
import com.nasyith.githubuserv2.ui.follow.FollowViewModel
import com.nasyith.githubuserv2.ui.main.MainViewModel
import com.nasyith.githubuserv2.ui.splash.SplashViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
            return FollowViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}