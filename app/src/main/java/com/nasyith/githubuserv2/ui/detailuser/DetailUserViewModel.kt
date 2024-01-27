package com.nasyith.githubuserv2.ui.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasyith.githubuserv2.data.UserRepository
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser
import com.nasyith.githubuserv2.data.remote.response.DetailUserResponse
import com.nasyith.githubuserv2.util.Event
import kotlinx.coroutines.launch

class DetailUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

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

    fun setDataDetailUser(detailUser: DetailUserResponse) {
        _detailUser.value = detailUser
    }

    fun setError(message: String) {
        _isError.value = Event(message)
    }

    fun findDetailUser(username: String) = userRepository.findDetailUser(username)

    fun getFavoriteUserByUsername(username: String) =
        userRepository.getFavoriteUserByUsername(username)

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            userRepository.insertFavoriteUser(favoriteUser)
        }
    }

    fun deleteFavoriteUser(username: String) {
        viewModelScope.launch {
            userRepository.deleteFavoriteUser(username)
        }
    }
}