package com.vibs.githubapidemo

interface GitHubNavigation {
    fun onNavigation(pageId: Int)
    fun getCurrentPAgeCount(): Int
}