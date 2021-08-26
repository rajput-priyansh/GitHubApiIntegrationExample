package com.vibs.githubapidemo.fragments

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vibs.githubapidemo.GitHubNavigation
import com.vibs.githubapidemo.R
import com.vibs.githubapidemo.databinding.FragmentRepositoryDetailsBinding
import com.vibs.githubapidemo.viewmodels.MainViewModel

class RepositoryDetailsFragment : Fragment(R.layout.fragment_repository_details) {

    private lateinit var binding: FragmentRepositoryDetailsBinding

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

        binding = FragmentRepositoryDetailsBinding.bind(view)

        observer()
    }

    /**
     * Use to observe the selected Repository
     */
    private fun observer() {
        viewModel.selectedRepository.observe(viewLifecycleOwner, { repositoryItem ->
            binding.tvName.text = repositoryItem.name ?: ""

            if (!repositoryItem.owner?.avatarUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(repositoryItem.owner?.avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfile)
            }

            binding.tvDesc.text = repositoryItem.description ?: ""

            if (!repositoryItem.htmlUrl.isNullOrEmpty()) {
                val endLen = repositoryItem.htmlUrl.length
                val spannableString = SpannableString(repositoryItem.htmlUrl)

                val openLink = object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        mCallback.onNavigation(R.id.actionDetailsToWebView)
                    }
                }

                spannableString.setSpan(openLink, 0, endLen, 0)
                spannableString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(requireContext(), R.color.text_active)
                    ), 0, endLen, 0
                )

                spannableString.setSpan(
                    BackgroundColorSpan(
                        ContextCompat.getColor(requireContext(), android.R.color.transparent)
                    ), 0, endLen, 0
                )

                binding.tvProjectLink.movementMethod = LinkMovementMethod.getInstance()
                binding.tvProjectLink.setText(spannableString, TextView.BufferType.SPANNABLE)
            }

        })
    }

}