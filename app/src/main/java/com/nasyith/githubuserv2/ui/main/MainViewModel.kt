package com.nasyith.githubuserv2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasyith.githubuserv2.data.UserRepository
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.util.Event
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<UserItem?>?>()
    val users: LiveData<List<UserItem?>?> = _users

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

    fun setDataUsers(users: List<UserItem?>?) {
        _users.value = users
    }

    fun setError(message: String) {
        _isError.value = Event(message)
    }

    fun findUser(username: String) = userRepository.findUser(username)

    fun getThemeSetting(): LiveData<Boolean> = userRepository.getThemeSetting()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            userRepository.saveThemeSetting(isDarkModeActive)
        }
    }
}