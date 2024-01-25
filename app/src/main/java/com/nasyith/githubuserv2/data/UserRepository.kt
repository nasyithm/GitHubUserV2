package com.nasyith.githubuserv2.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser
import com.nasyith.githubuserv2.data.local.room.UserDao
import com.nasyith.githubuserv2.data.remote.response.DetailUserResponse
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.data.remote.response.UserResponse
import com.nasyith.githubuserv2.data.remote.retrofit.ApiService
import com.nasyith.githubuserv2.helper.SettingPreferences

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val preferences: SettingPreferences
) {
    fun findUser(username: String): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("UserRepository", "findUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun findDetailUser(username: String): LiveData<Result<DetailUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("UserRepository", "findDetailUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun findFollowersUser(username: String): LiveData<Result<List<UserItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowersUser(username)
            if (response.isNotEmpty()) emit(Result.Success(response))
            else emit(Result.Error("Followers Not Found"))
        } catch (e: Exception) {
            Log.d("UserRepository", "findFollowersUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun findFollowingUser(username: String): LiveData<Result<List<UserItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowingUser(username)
            if (response.isNotEmpty()) emit(Result.Success(response))
            else emit(Result.Error("Following Not Found"))
        } catch (e: Exception) {
            Log.d("UserRepository", "findFollowingUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = userDao.getFavoriteUsers()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = userDao.getFavoriteUserByUsername(username)

    suspend fun insertFavoriteUser(favoriteUser: FavoriteUser) = userDao.insertFavoriteUser(favoriteUser)

    suspend fun deleteFavoriteUser(username: String) = userDao.deleteFavoriteUser(username)

    fun getThemeSetting(): LiveData<Boolean> = preferences.getThemeSetting().asLiveData()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = preferences.saveThemeSetting(isDarkModeActive)

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            preferences: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, preferences)
            }.also { instance = it }
    }
}