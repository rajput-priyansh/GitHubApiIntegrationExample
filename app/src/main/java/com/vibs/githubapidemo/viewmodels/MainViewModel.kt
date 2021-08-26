package com.vibs.githubapidemo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vibs.githubapidemo.api.ResponseManager
import com.vibs.githubapidemo.models.ResponseSearchRepositories
import com.vibs.githubapidemo.repository.MainRepository
import androidx.lifecycle.viewModelScope
import com.vibs.githubapidemo.database.Repository
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepository by lazy {
        MainRepository(application)
    }


    private val _responseRepositories = MutableLiveData<ResponseSearchRepositories>()

    val responseRepositories: LiveData<ResponseSearchRepositories> = _responseRepositories

    init {
    }

    /**
     * Set repositories response
     */
    fun setResponseRepositories(data: ResponseSearchRepositories?) {
        _responseRepositories.value = data
    }

    /**
     * Make GitHub API to search through repositories
     */
    fun apiGetGitHubRepository(query: String, perPage: Int, page: Int): LiveData<ResponseManager<ResponseSearchRepositories>> {
        val liveData = MutableLiveData<ResponseManager<ResponseSearchRepositories>>()
        viewModelScope.launch {
            liveData.value = repository.getGitHubRepository(query, perPage, page).value
        }
        return liveData
    }

    /**
     * get repository list from local DB
     */
    fun getGitHubRepositoryFromDb(): LiveData<List<Repository>> {
        val liveData = MutableLiveData<List<Repository>>()
        viewModelScope.launch {
            liveData.value = repository.getGitHubRepositoryFromDb().value
        }
        return liveData
    }

    /**
     * get repository list from local DB
     */
    fun insertGitHubRepositoriesToDb(repositories: List<Repository>) {
        viewModelScope.launch {
            repository.insertGitHubRepositoriesToDb(repositories)
        }
    }
    /**
     * remove repository list from local DB
     */
    fun deleteGitHubRepositoriesToDb() {
        viewModelScope.launch {
            repository.deleteGitHubRepositoriesToDb()
        }
    }
}