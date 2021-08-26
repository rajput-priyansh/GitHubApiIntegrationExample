package com.vibs.githubapidemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vibs.githubapidemo.R
import com.vibs.githubapidemo.models.RepositoryItem

class RepositoriesAdapter(private val listener: RepositoryListener,
                          private val currentList: ArrayList<RepositoryItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_NORMAL = 1
    }

    private var isLoaderVisible = false

    private lateinit var context: Context

    interface RepositoryListener {
        fun onRepositoryDetails(repository: RepositoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return if (viewType == VIEW_TYPE_NORMAL) {
            RepositoryViewHolder.from(parent)
        } else {
            ProgressViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL){
            (holder as RepositoryViewHolder).bind(currentList[position], this, context)
        }
    }

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == currentList.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addLoading() {
        isLoaderVisible = true
        currentList.add(RepositoryItem(id = -1))
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeLoading() {
        isLoaderVisible = false
        val item = currentList.find { r -> r.id == -1 }
        if (item != null) {
            currentList.remove(item)
        }
        notifyDataSetChanged()
    }

    open class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val mainCard: CardView = view.findViewById(R.id.mainCard)
        private val ivProfile: ImageView = view.findViewById(R.id.ivProfile)
        private val tvName: TextView = view.findViewById(R.id.tvName)
        private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        private val tvProjectLink: TextView = view.findViewById(R.id.tvProjectLink)

        fun bind(repositoryItem: RepositoryItem, repositoryAdapter: RepositoriesAdapter,
            context: Context) {

            mainCard.setOnClickListener {
                repositoryAdapter.listener.onRepositoryDetails(repositoryItem)
            }

            tvName.text = repositoryItem.name ?: ""

            if (!repositoryItem.owner?.avatarUrl.isNullOrEmpty()) {
                Glide.with(context)
                    .load(repositoryItem.owner?.avatarUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfile)
            }

            tvDescription.text = repositoryItem.description ?: ""

            tvProjectLink.text = repositoryItem.htmlUrl ?: ""

        }

        companion object {
            fun from(parent: ViewGroup): RepositoryViewHolder {
                return RepositoryViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_repository_card,
                            parent,
                            false)
                )
            }
        }

    }

    open class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): ProgressViewHolder {
                return ProgressViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_progress,
                            parent,
                            false)
                )
            }
        }

    }

}