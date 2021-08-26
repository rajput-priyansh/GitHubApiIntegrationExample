package com.vibs.githubapidemo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vibs.githubapidemo.GitHubNavigation
import com.vibs.githubapidemo.R
import com.vibs.githubapidemo.adapter.RepositoriesAdapter
import com.vibs.githubapidemo.databinding.FragmentHomeBinding
import com.vibs.githubapidemo.models.RepositoryItem
import com.vibs.githubapidemo.viewmodels.MainViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var mCallback: GitHubNavigation

    private lateinit var adapter: RepositoriesAdapter

    private val viewModel: MainViewModel by activityViewModels()

    private val repositories = arrayListOf<RepositoryItem>()

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

        initUi()

        observer()

        //adapter.differ.submitList(it?.kinItem ?: arrayListOf())
    }

    private fun observer() {
        viewModel.responseRepositories.observe(viewLifecycleOwner, {
            Log.e("TESTP", "observer() called")
            if (it.items!= null && it.items.isNotEmpty()) {
                if (mCallback.getCurrentPAgeCount() == 1) {
                    repositories.clear()
                }
                repositories.addAll(it.items as ArrayList<RepositoryItem>)

                adapter.differ.submitList(repositories)
            }
        })
    }

    private fun initUi() {
        binding.rvRepositories.layoutManager = LinearLayoutManager(requireContext())

        adapter = RepositoriesAdapter(object : RepositoriesAdapter.RepositoryListener {
            override fun onRepositoryDetails(repository: RepositoryItem) {
                mCallback.onNavigation(R.id.actionHomeToDetails)
            }
        })

        binding.rvRepositories.adapter = adapter
    }

}