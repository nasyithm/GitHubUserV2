package com.nasyith.githubuserv2.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasyith.githubuserv2.R
import com.nasyith.githubuserv2.data.Result
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.databinding.ActivityMainBinding
import com.nasyith.githubuserv2.helper.ViewModelFactory
import com.nasyith.githubuserv2.ui.adapter.UserAdapter
import com.nasyith.githubuserv2.ui.detailuser.DetailUserActivity
import com.nasyith.githubuserv2.ui.favoriteuser.FavoriteUserActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var isDarkModeActive = false
    private val username = "nasyith"

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.searchBar)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        setupSearchView()
        getThemeSetting()
        getUserData()
        showError()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuFavorite -> {
                val showFavoriteUserIntent = Intent(
                    this@MainActivity, FavoriteUserActivity::class.java
                )
                startActivity(showFavoriteUserIntent)
            }
            R.id.menuTheme -> {
                mainViewModel.saveThemeSetting(isDarkModeActive)
                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        mainViewModel.getThemeSetting().observe(this) { isDarkMode: Boolean ->
            if (!isDarkMode) {
                menu?.findItem(R.id.menuTheme)?.icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_light_mode_24)
                menu?.findItem(R.id.menuFavorite)?.icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_favorite_night_24)
            } else {
                menu?.findItem(R.id.menuTheme)?.icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_night_mode_24)
                menu?.findItem(R.id.menuFavorite)?.icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val username = searchView.text.toString()
                    searchView.hide()
                    findUser(username)
                    false
                }
        }
    }

    private fun getThemeSetting() {
        mainViewModel.getThemeSetting().observe(this) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                 this.isDarkModeActive = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                this.isDarkModeActive = true
            }
        }
    }

    private fun getUserData() {
        mainViewModel.dataLoaded.observe(this) { isDataLoaded ->
            if (!isDataLoaded) {
                findUser(username)
            } else {
                mainViewModel.users.observe(this) {
                    setUserData(it)
                }
            }
        }
    }

    private fun findUser(username: String) {
        mainViewModel.findUser(username).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    setUserData(result.data.items)
                    mainViewModel.setDataUsers(result.data.items)
                    mainViewModel.setDataLoaded(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    mainViewModel.setError(result.error)
                    mainViewModel.setDataLoaded(true)
                }
            }
        }
    }

    private fun setUserData(users: List<UserItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                showDetailUser(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError() {
        mainViewModel.isError.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDetailUser(user: UserItem) {
        val showDetailUserIntent = Intent(
            this@MainActivity, DetailUserActivity::class.java
        )
        showDetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(showDetailUserIntent)
    }
}