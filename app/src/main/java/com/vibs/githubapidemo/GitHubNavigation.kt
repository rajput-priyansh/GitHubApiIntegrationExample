package com.vibs.githubapidemo

interface GitHubNavigation {
    fun onNavigation(pageId: Int)
    fun getCurrentPageCount(): Int
    fun setCurrentPageCount(page: Int)
    fun onSearchQuery(query: String)
}