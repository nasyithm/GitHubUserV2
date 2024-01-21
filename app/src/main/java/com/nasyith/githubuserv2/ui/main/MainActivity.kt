package com.nasyith.githubuserv2.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasyith.githubuserv2.R
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.databinding.ActivityMainBinding
import com.nasyith.githubuserv2.helper.PreferencesViewModelFactory
import com.nasyith.githubuserv2.helper.SettingPreferences
import com.nasyith.githubuserv2.helper.dataStore
import com.nasyith.githubuserv2.ui.adapter.UserAdapter
import com.nasyith.githubuserv2.ui.detailuser.DetailUserActivity
import com.nasyith.githubuserv2.ui.favoriteuser.FavoriteUserActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var isDarkModeActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.searchBar)

        val pref = SettingPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.users.observe(this) { users ->
            setUserData(users)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val username = searchView.text.toString()
                    searchView.hide()
                    mainViewModel.findUser(username)
                    false
                }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isError.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showError(it)
            }
        }

        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                this.isDarkModeActive = false
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                this.isDarkModeActive = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuFavorite -> {
                val showFavoriteUserIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
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
                    ContextCompat.getDrawable(this, R.drawable.baseline_favorite_white_24)
            } else {
                menu?.findItem(R.id.menuTheme)?.icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_night_mode_24)
                menu?.findItem(R.id.menuFavorite)?.icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24)
            }
        }
        return super.onPrepareOptionsMenu(menu)
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

    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDetailUser(user: UserItem) {
        val showDetailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        showDetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(showDetailUserIntent)
    }
}