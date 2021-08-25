package com.vibs.githubapidemo.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vibs.githubapidemo.GitHubNavigation
import com.vibs.githubapidemo.R
import com.vibs.githubapidemo.databinding.FragmentRepositoryDetailsBinding

class RepositoryDetailsFragment: Fragment(R.layout.fragment_repository_details) {

    private lateinit var binding: FragmentRepositoryDetailsBinding

    private lateinit var mCallback: GitHubNavigation

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

        binding = FragmentRepositoryDetailsBinding.bind(view)
    }

}