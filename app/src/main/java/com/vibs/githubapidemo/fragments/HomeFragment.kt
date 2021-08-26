package com.vibs.githubapidemo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vibs.githubapidemo.GitHubNavigation
import com.vibs.githubapidemo.R
import com.vibs.githubapidemo.adapter.RepositoriesAdapter
import com.vibs.githubapidemo.databinding.FragmentHomeBinding
import com.vibs.githubapidemo.listener.PaginationListener
import com.vibs.githubapidemo.models.Owner
import com.vibs.githubapidemo.models.RepositoryItem
import com.vibs.githubapidemo.viewmodels.MainViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var mCallback: GitHubNavigation

    private lateinit var adapter: RepositoriesAdapter

    private val viewModel: MainViewModel by activityViewModels()

    private val repositories = arrayListOf<RepositoryItem>()
    private val repositoriesFromDb = arrayListOf<RepositoryItem>()

    private var isLastPage = false
    private var isLoading = false

    private lateinit var handler: Handler

    override fun onAttach(context: Context) {
        try {
            mCallback = activity as GitHubNavigation
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString() + " must implement HeadlineListener"
            )
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        initData()

        initUi()

        observer()
    }

    private fun initData() {
        handler = Handler(Looper.getMainLooper())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observer() {
        viewModel.responseRepositories.observe(viewLifecycleOwner, {
            if (it.items != null) {
                if (mCallback.getCurrentPageCount() == 1) {
                    repositories.clear()
                    isLastPage = false
                }
                repositories.addAll(it.items as ArrayList<RepositoryItem>)
                adapter.notifyDataSetChanged()

                // check weather is last page or not
                if (repositories.size < it.totalCount ?: 0) {
                    adapter.addLoading()
                } else {
                    adapter.removeLoading()
                    adapter.notifyDataSetChanged()
                    isLastPage = true
                }
                isLoading = false
            }
        })

        viewModel.getGitHubRepositoryFromDb().observe(viewLifecycleOwner, {
            if (it != null && it.isNotEmpty()) {
                for (item in it) {
                    repositoriesFromDb.add(
                        RepositoryItem(
                            id = item.id,
                            name = item.name,
                            description = item.description,
                            htmlUrl = item.ProjectUrl,
                            owner = Owner(avatarUrl = item.avatarUrl)
                        )
                    )
                }
            }
            //Load the data in case of network call get fail
            if (repositories.isEmpty() && repositoriesFromDb.isNotEmpty()) {
                repositories.clear()
                repositories.addAll(repositoriesFromDb)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initUi() {
        binding.rvRepositories.layoutManager = LinearLayoutManager(requireContext())

        adapter = RepositoriesAdapter(object : RepositoriesAdapter.RepositoryListener {
            override fun onRepositoryDetails(repository: RepositoryItem) {
                mCallback.onNavigation(R.id.actionHomeToDetails)
            }
        }, repositories)

        binding.rvRepositories.adapter = adapter

        binding.rvRepositories.addOnScrollListener(object :
            PaginationListener(binding.rvRepositories.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                mCallback.setCurrentPageCount((mCallback.getCurrentPageCount() + 1))
                mCallback.onSearchQuery(binding.svQuery.query.toString())
            }

            override fun isLastPage(): Boolean {
                return isLastPage;
            }

            override fun isLoading(): Boolean {
                return isLoading;
            }
        });

        binding.svQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed({
                        mCallback.setCurrentPageCount(1);
                        mCallback.onSearchQuery(newText)
                    }, 1500)
                }
                return false
            }

        })
    }

}