package com.nasyith.githubuserv2.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasyith.githubuserv2.ui.main.MainViewModel
import java.lang.IllegalArgumentException

class PreferencesViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}