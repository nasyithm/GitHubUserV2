package com.nasyith.githubuserv2.ui.follow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasyith.githubuserv2.data.remote.response.UserItem
import com.nasyith.githubuserv2.databinding.FragmentFollowBinding
import com.nasyith.githubuserv2.ui.adapter.UserAdapter
import com.nasyith.githubuserv2.ui.follow.FollowViewModel.Companion.position
import com.nasyith.githubuserv2.ui.follow.FollowViewModel.Companion.username
import com.nasyith.githubuserv2.ui.detailuser.DetailUserActivity

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowViewModel::class.java]
        followViewModel.followers.observe(requireActivity()) { followers ->
            if (followers != null) {
                setFollowersUserData(followers)
            }
        }

        followViewModel.following.observe(requireActivity()) { following ->
            if (following != null) {
                setFollowingUserData(following)
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        followViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        followViewModel.isError.observe(requireActivity()) { it ->
            it.getContentIfNotHandled()?.let {
                showError(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setFollowersUserData(followers: List<UserItem>) {
        val adapter = UserAdapter()
        adapter.submitList(followers)
        binding.rvFollow.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                showDetailUser(data)
            }
        })
    }

    private fun setFollowingUserData(following: List<UserItem>) {
        val adapter = UserAdapter()
        adapter.submitList(following)
        binding.rvFollow.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                showDetailUser(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }

    private fun showError(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
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