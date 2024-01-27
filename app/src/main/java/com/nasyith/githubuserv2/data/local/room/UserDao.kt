package com.nasyith.githubuserv2.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser

@Dao
interface UserDao {
    @Query("SELECT * from FavoriteUser ORDER BY username ASC")
    fun getFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteUser(favoriteUser: FavoriteUser)

    @Update
    suspend fun updateFavoriteUser(favoriteUser: FavoriteUser)

    @Query("DELETE FROM FavoriteUser WHERE username = :username")
    suspend fun deleteFavoriteUser(username: String)
}