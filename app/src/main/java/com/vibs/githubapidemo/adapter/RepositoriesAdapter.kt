package com.vibs.githubapidemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vibs.githubapidemo.R
import com.vibs.githubapidemo.models.RepositoryItem

class RepositoriesAdapter(private val listener: RepositoryListener) :
    RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder>() {

    private lateinit var context: Context

    interface RepositoryListener {
        fun onRepositoryDetails(repository: RepositoryItem)
    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<RepositoryItem>() {
        override fun areItemsTheSame(
            oldItem: RepositoryItem,
            newItem: RepositoryItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RepositoryItem,
            newItem: RepositoryItem,
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        context = parent.context
        return RepositoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position], this, context)
    }

    override fun getItemCount() = differ.currentList.size
    

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

}