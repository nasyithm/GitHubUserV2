package com.nasyith.githubuserv2.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nasyith.githubuserv2.data.UserRepository
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.util.Event

class FollowViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _followUser = MutableLiveData<List<UserItem>>()
    val followUser: LiveData<List<UserItem>> = _followUser

    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean> = _dataLoaded

    private val _isError = MutableLiveData<Event<String>>()
    val isError: LiveData<Event<String>> = _isError

    init {
        setDataLoaded(false)
    }

    fun setDataLoaded(isDataLoaded: Boolean) {
        _dataLoaded.value = isDataLoaded
    }

    fun setDataFollowUser(followUser: List<UserItem>) {
        _followUser.value = followUser
    }

    fun setError(message: String) {
        _isError.value = Event(message)
    }

    fun findFollowersUser(username: String) = userRepository.findFollowersUser(username)

    fun findFollowingUser(username: String) = userRepository.findFollowingUser(username)
}