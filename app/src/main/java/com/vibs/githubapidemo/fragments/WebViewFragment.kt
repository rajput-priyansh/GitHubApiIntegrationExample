package com.vibs.githubapidemo.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vibs.githubapidemo.GitHubNavigation
import com.vibs.githubapidemo.R
import com.vibs.githubapidemo.databinding.FragmentWebViewBinding
import com.vibs.githubapidemo.viewmodels.MainViewModel

class WebViewFragment: Fragment(R.layout.fragment_web_view) {

    private lateinit var binding: FragmentWebViewBinding
    private lateinit var mCallback: GitHubNavigation

    private val viewModel: MainViewModel by activityViewModels()

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

        binding = FragmentWebViewBinding.bind(view)

        initUi()

        observer()
    }

    private fun initUi() {
        binding.webView.settings.javaScriptEnabled = true

        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.databaseEnabled = true
        binding.webView.settings.allowFileAccess = true
    }

    /**
     * Use to observe the selected Repository
     */
    private fun observer() {
        viewModel.selectedRepository.observe(viewLifecycleOwner, { repositoryItem ->

            if (!repositoryItem.htmlUrl.isNullOrEmpty()) {
                binding.webView.loadUrl(repositoryItem.htmlUrl)
            }

        })
    }

}