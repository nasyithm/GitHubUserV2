package com.nasyith.githubuserv2.ui.detailuser

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nasyith.githubuserv2.R
import com.nasyith.githubuserv2.data.Result
import com.nasyith.githubuserv2.data.local.entity.FavoriteUser
import com.nasyith.githubuserv2.data.remote.response.DetailUserResponse
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.databinding.ActivityDetailUserBinding
import com.nasyith.githubuserv2.helper.ViewModelFactory
import com.nasyith.githubuserv2.ui.adapter.SectionsPagerAdapter

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    private var favoriteUser: FavoriteUser? = null
    private var username = "username"

    private val detailUserViewModel: DetailUserViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_USER, UserItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER)
        }

        username = user?.login.toString()

        detailUserViewModel.dataLoaded.observe(this) { isDataLoaded ->
            if (!isDataLoaded) {
                detailUserViewModel.findDetailUser(username).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setDetailUserData(result.data)
                            favoriteUser = FavoriteUser(result.data.login.toString(), result.data.id!!, result.data.avatarUrl.toString())
                            detailUserViewModel.setDataDetailUser(result.data)
                            detailUserViewModel.setDataLoaded(true)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            detailUserViewModel.setError(result.error)
                            detailUserViewModel.setDataLoaded(true)
                        }
                    }
                }
            } else {
                detailUserViewModel.detailUser.observe(this) {
                    setDetailUserData(it)
                    favoriteUser = FavoriteUser(it.login.toString(), it.id!!, it.avatarUrl.toString())
                }
            }
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user?.login.toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailUserViewModel.detailUser.observe(this) {
            if (it.login != null) {
                binding.btnFavoriteUser.isEnabled = true

                detailUserViewModel.getFavoriteUserByUsername(username).observe(this) { favUser ->
                    if (favUser != null) {
                        binding.btnFavoriteUser.setImageResource(R.drawable.baseline_favorite_24)
                        binding.btnFavoriteUser.setOnClickListener {
                            detailUserViewModel.deleteFavoriteUser(favoriteUser?.username.toString())
                        }
                    } else {
                        binding.btnFavoriteUser.setImageResource(R.drawable.baseline_favorite_border_24)
                        binding.btnFavoriteUser.setOnClickListener {
                            detailUserViewModel.insertFavoriteUser(favoriteUser as FavoriteUser)
                        }
                    }
                }
            }
        }

        detailUserViewModel.isError.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                showError(it)
            }
        }
    }

    private fun setDetailUserData(detailUser: DetailUserResponse) {
        Glide.with(this)
            .load(detailUser.avatarUrl)
            .transform(CircleCrop())
            .into(binding.detailAvatar)
        binding.detailUsername.text = detailUser.login
        binding.detailName.text = detailUser.name
        binding.detailFollowers.text = getString(R.string.followers, detailUser.followers.toString())
        binding.detailFollowing.text = getString(R.string.following, detailUser.following.toString())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers,
            R.string.tab_text_following
        )
    }
}