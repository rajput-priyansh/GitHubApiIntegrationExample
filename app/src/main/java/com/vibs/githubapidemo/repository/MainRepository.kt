package com.vibs.githubapidemo.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vibs.dynamicview.api.ApiRepository
import com.vibs.githubapidemo.api.ResponseManager
import com.vibs.githubapidemo.database.MainDatabase
import com.vibs.githubapidemo.database.Repository
import com.vibs.githubapidemo.models.ResponseSearchRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.LiveData

class MainRepository(application: Application) {

    private var globalApi = ApiRepository().getGlobalInstance()

    private var mainDatabase = MainDatabase.getDatabaseClient(application)

    private var allRepository: LiveData<List<Repository>> = mainDatabase.repositoryDto().getAllRepository()

    fun getAllRepositories(): LiveData<List<Repository>> {
        return allRepository
    }

    /**
     * Make APi call to get GitHub Repository
     * query: Search String
     * return - LiveData object
     */
    suspend fun getGitHubRepository(query: String, perPage: Int, page: Int)
            : MutableLiveData<ResponseManager<ResponseSearchRepositories>> {

        val liveData = MutableLiveData<ResponseManager<ResponseSearchRepositories>>()

        try {

            val response =
                withContext(Dispatchers.IO) {
                    globalApi.getGitHubRepository(query, perPage, page)
                }

            withContext(Dispatchers.Main) {
                if (response.message.isNullOrEmpty()) {
                    liveData.value = ResponseManager.Success(response)
                } else {
                    liveData.value = ResponseManager.Error(response.message ?: "")
                }
            }

        } catch (e: Exception) {
            ResponseManager.manageException(liveData, e)
        }

        return liveData
    }

    /**
     * insert GitHub Repositories to Database
     */
    suspend fun insertGitHubRepositoriesToDb(repositories: List<Repository>) {
        try {
            withContext(Dispatchers.IO) {
                mainDatabase.repositoryDto().insertAll(repositories)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    /**
     * delete GitHub Repositories to Database
     */
    suspend fun deleteGitHubRepositoriesToDb() {
        try {
            withContext(Dispatchers.IO) {
                mainDatabase.repositoryDto().deleteAll()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}