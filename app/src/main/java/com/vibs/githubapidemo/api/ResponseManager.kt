package com.vibs.githubapidemo.api

import androidx.lifecycle.MutableLiveData
import com.vibs.githubapidemo.BuildConfig
import java.lang.Exception
import java.net.ConnectException

sealed class ResponseManager<out T : Any> {

    data class Success<T : Any>(val data: T) : ResponseManager<T>()
    data class Error(val message: String = "", val stringID: Int = 0) : ResponseManager<Nothing>()

    data class Loading<T : Any>(val data: T) : ResponseManager<T>()
    companion object {
        fun <T : Any> manageException(
            liveData: MutableLiveData<ResponseManager<T>>,
            exception: Exception
        ) {
            try {
                if (exception is ConnectException) {
                    liveData.value =
                        Error(BuildConfig.API_CONNECTION_ERROR_MSG)
                } else {
                    liveData.value = Error(ApiError(exception).message)
                }
            } catch (e: Exception) {
                liveData.value = Error(BuildConfig.API_PARSE_ERROR_MSG)
            }
        }
    }
}