package com.nasyith.githubuserv2.data.remote.retrofit

import com.nasyith.githubuserv2.data.remote.response.DetailUserResponse
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    suspend fun getUser(@Query("q") q: String): UserResponse

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowersUser(@Path("username") username: String): List<UserItem>

    @GET("users/{username}/following")
    suspend fun getFollowingUser(@Path("username") username: String): List<UserItem>
}