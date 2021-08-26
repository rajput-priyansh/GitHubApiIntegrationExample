package com.vibs.githubapidemo.api

import com.vibs.githubapidemo.models.ResponseSearchRepositories
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("search/repositories")
    suspend fun getGitHubRepository(
        @Query("q") query: String,
        @Query("per_page") paePage: Int,
        @Query("page") page: Int
    ): ResponseSearchRepositories
}