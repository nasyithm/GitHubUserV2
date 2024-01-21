package com.nasyith.githubuserv2.ui.favoriteuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nasyith.githubuserv2.data.FavoriteUserRepository
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getFavoriteUsers()
}