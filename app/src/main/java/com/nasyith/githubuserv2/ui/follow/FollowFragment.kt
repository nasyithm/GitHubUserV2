package com.nasyith.githubuserv2.ui.follow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasyith.githubuserv2.data.Result
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.databinding.FragmentFollowBinding
import com.nasyith.githubuserv2.helper.ViewModelFactory
import com.nasyith.githubuserv2.ui.adapter.UserAdapter
import com.nasyith.githubuserv2.ui.detailuser.DetailUserActivity

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private var position = 1
    private var username = "username"

    private val followViewModel: FollowViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        if (position == 1) getFollowersUserData() else getFollowingUserData()

        showError()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getFollowersUserData() {
        followViewModel.dataLoaded.observe(viewLifecycleOwner) { isDataLoaded ->
            if (!isDataLoaded) {
                followViewModel.findFollowersUser(username).observe(viewLifecycleOwner) { followers ->
                    when (followers) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setFollowUserData(followers.data)
                            followViewModel.setDataFollowUser(followers.data)
                            followViewModel.setDataLoaded(true)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            followViewModel.setError(followers.error)
                            followViewModel.setDataLoaded(true)
                        }
                    }
                }
            } else {
                followViewModel.followUser.observe(viewLifecycleOwner) {
                    setFollowUserData(it)
                }
            }
        }
    }

    private fun getFollowingUserData() {
        followViewModel.dataLoaded.observe(viewLifecycleOwner) { isDataLoaded ->
            if (!isDataLoaded) {
                followViewModel.findFollowingUser(username).observe(viewLifecycleOwner) { following ->
                    when (following) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setFollowUserData(following.data)
                            followViewModel.setDataFollowUser(following.data)
                            followViewModel.setDataLoaded(true)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            followViewModel.setError(following.error)
                            followViewModel.setDataLoaded(true)
                        }
                    }
                }
            } else {
                followViewModel.followUser.observe(viewLifecycleOwner) {
                    setFollowUserData(it)
                }
            }
        }
    }

    private fun setFollowUserData(follow: List<UserItem>) {
        val adapter = UserAdapter()
        adapter.submitList(follow)
        binding.rvFollow.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                showDetailUser(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }

    private fun showError() {
        followViewModel.isError.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDetailUser(user: UserItem) {
        val showDetailUserIntent = Intent(requireActivity(), DetailUserActivity::class.java)
        showDetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(showDetailUserIntent)
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}