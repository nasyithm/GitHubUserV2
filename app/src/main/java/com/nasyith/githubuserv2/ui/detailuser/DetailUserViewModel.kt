package com.nasyith.githubuserv2.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nasyith.githubuserv2.data.FavoriteUserRepository
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser
import com.nasyith.githubuserv2.data.remote.response.DetailUserResponse
import com.nasyith.githubuserv2.data.remote.retrofit.ApiConfig
import com.nasyith.githubuserv2.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailUser: LiveData<DetailUserResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Event<String>>()
    val isError: LiveData<Event<String>> = _isError

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    init {
        findDetailUser(username)
    }

    private fun findDetailUser(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(user)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isError.value = Event("Username not found")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _isError.value = Event("Unable to connect internet")
            }
        })
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = mFavoriteUserRepository.getFavoriteUserByUsername(username)

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insertFavoriteUser(favoriteUser)
    }

    fun deleteFavoriteUser(username: String) = mFavoriteUserRepository.deleteFavoriteUser(username)

    companion object {
        private const val TAG = "DetailUserActivity"
        var username = "username"
    }
}