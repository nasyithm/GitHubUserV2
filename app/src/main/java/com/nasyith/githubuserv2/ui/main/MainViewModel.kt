package com.nasyith.githubuserv2.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.data.remote.response.UserResponse
import com.nasyith.githubuserv2.data.remote.retrofit.ApiConfig
import com.nasyith.githubuserv2.helper.SettingPreferences
import com.nasyith.githubuserv2.util.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _users = MutableLiveData<List<UserItem?>?>()
    val users: LiveData<List<UserItem?>?> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Event<String>>()
    val isError: LiveData<Event<String>> = _isError

    init {
        findUser(USERNAME)
    }

    fun findUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.body()?.items?.isNotEmpty() == true) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = responseBody.items
                    }
                } else {
                    Log.e(TAG, "onFailure: Username Not Found")
                    _isError.value = Event("Username not found")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _isError.value = Event("Unable to connect internet")
            }
        })
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private var USERNAME = "nasyith"
    }
}