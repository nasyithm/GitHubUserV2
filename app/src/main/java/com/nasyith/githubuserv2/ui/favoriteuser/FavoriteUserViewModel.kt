package com.nasyith.githubuserv2.ui.favoriteuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nasyith.githubuserv2.data.UserRepository
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser
import com.nasyith.githubuserv2.util.Event

class FavoriteUserViewModel(private val userRepository: UserRepository) : ViewModel() {

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
    fun setError(message: String) {
        _isError.value = Event(message)
    }
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = userRepository.getFavoriteUsers()
}