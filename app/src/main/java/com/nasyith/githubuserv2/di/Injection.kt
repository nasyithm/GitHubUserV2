package com.nasyith.githubuserv2.di

import android.content.Context
import com.nasyith.githubuserv2.data.UserRepository
import com.nasyith.githubuserv2.data.local.room.UserDatabase
import com.nasyith.githubuserv2.data.remote.retrofit.ApiConfig
import com.nasyith.githubuserv2.helper.SettingPreferences
import com.nasyith.githubuserv2.helper.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userDatabase = UserDatabase.getInstance(context)
        val favoriteUserDao = userDatabase.userDao()
        val preferences = SettingPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, favoriteUserDao, preferences)
    }
}