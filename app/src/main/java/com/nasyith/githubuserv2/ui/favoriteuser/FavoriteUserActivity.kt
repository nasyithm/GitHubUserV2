package com.nasyith.githubuserv2.ui.favoriteuser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.databinding.ActivityFavoriteUserBinding
import com.nasyith.githubuserv2.helper.ViewModelFactory
import com.nasyith.githubuserv2.ui.adapter.UserAdapter
import com.nasyith.githubuserv2.ui.detailuser.DetailUserActivity

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    private val favoriteUserViewModel: FavoriteUserViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getFavoriteUsersData()

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteUser.addItemDecoration(itemDecoration)

        showError()
    }

    private fun getFavoriteUsersData() {
        favoriteUserViewModel.getFavoriteUsers().observe(this) { users ->
            val items = arrayListOf<UserItem>()
            val adapter = UserAdapter()

            if (users.isNotEmpty()) {
                users.map {
                    val item = UserItem(login = it.username, id = it.id, avatarUrl = it.avatarUrl)
                    items.add(item)
                }
            } else {
                favoriteUserViewModel.dataLoaded.observe(this) { isDataLoaded ->
                    if (!isDataLoaded) {
                        favoriteUserViewModel.setError("You don't have favorite users")
                        favoriteUserViewModel.setDataLoaded(true)
                    }
                }
            }

            adapter.submitList(items)
            binding.rvFavoriteUser.adapter = adapter

            adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserItem) {
                    showDetailUser(data)
                }
            })
        }
    }

    private fun showDetailUser(user: UserItem) {
        val showDetailUserIntent = Intent(
            this@FavoriteUserActivity, DetailUserActivity::class.java
        )
        showDetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(showDetailUserIntent)
    }

    private fun showError() {
        favoriteUserViewModel.isError.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}