package com.nasyith.githubuserv2.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(
	@field:SerializedName("items")
	val items: List<UserItem?>? = null
)

@Parcelize
data class UserItem(
	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,
): Parcelable
