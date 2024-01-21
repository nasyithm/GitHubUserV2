package com.nasyith.githubuserv2.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser
import com.nasyith.githubuserv2.data.local.room.FavoriteUserDao
import com.nasyith.githubuserv2.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getFavoriteUsers()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = mFavoriteUserDao.getFavoriteUserByUsername(username)

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insertFavoriteUser(favoriteUser) }
    }

    fun deleteFavoriteUser(username: String) = executorService.execute { mFavoriteUserDao.deleteFavoriteUser(username) }
}