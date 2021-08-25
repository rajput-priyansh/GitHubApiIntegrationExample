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

class MainRepository(application: Application) {

    private var globalApi = ApiRepository().getGlobalInstance()

    private var mainDatabase = MainDatabase.getDatabaseClient(application)

    /**
     * Make APi call to get GitHub Repository
     * query: Search String
     * return - LiveData object
     */
    suspend fun getGitHubRepository(query: String)
            : MutableLiveData<ResponseManager<ResponseSearchRepositories>> {

        val liveData = MutableLiveData<ResponseManager<ResponseSearchRepositories>>()

        try {

            val response =
                withContext(Dispatchers.IO) {
                    globalApi.getGitHubRepository(query)
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
     * get GitHub Repository from Database
     */
    suspend fun getGitHubRepositoryFromDb(): MutableLiveData<List<Repository>> {
        val liveData = MutableLiveData<List<Repository>>()

        try {

            val repository =
                withContext(Dispatchers.IO) {
                    mainDatabase.repositoryDto().getAllRepository()
                }

            withContext(Dispatchers.Main) {
                liveData.value = repository
            }

        } catch (e: Exception) {
            liveData.value = null
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

}