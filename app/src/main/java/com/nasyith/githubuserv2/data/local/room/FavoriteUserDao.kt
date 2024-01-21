package com.nasyith.githubuserv2.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Query("SELECT * from FavoriteUser ORDER BY username ASC")
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteUser(favoriteUser: FavoriteUser)

    @Update
    fun updateFavoriteUser(favoriteUser: FavoriteUser)

    @Query("DELETE FROM FavoriteUser WHERE username = :username")
    fun deleteFavoriteUser(username: String)
}