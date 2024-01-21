package com.nasyith.githubuserv2.ui.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.data.remote.retrofit.ApiConfig
import com.nasyith.githubuserv2.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _followers = MutableLiveData<List<UserItem>?>()
    val followers: LiveData<List<UserItem>?> = _followers

    private val _following = MutableLiveData<List<UserItem>?>()
    val following: LiveData<List<UserItem>?> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Event<String>>()
    val isError: LiveData<Event<String>> = _isError

    init {
        if (position == 1) {
            findFollowersUser(username)
        } else {
            findFollowingUser(username)
        }
    }

    private fun findFollowersUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowersUser(username)
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if (response.body()?.isNotEmpty() == true) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followers.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isError.value = Event("Followers not found")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _isError.value = Event("Unable to connect internet")
            }
        })
    }

    private fun findFollowingUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowingUser(username)
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if (response.body()?.isNotEmpty() == true) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _following.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isError.value = Event("Following not found")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _isError.value = Event("Unable to connect internet")
            }
        })
    }

    companion object {
        private const val TAG = "FollowFragment"
        var position = 1
        var username = ""
    }
}