package com.vibs.githubapidemo.api

import com.google.gson.JsonParser
import retrofit2.HttpException
import java.lang.Exception

class ApiError constructor(error: Throwable) {
    var message = "An error occurred"
    private val default = "Something want wrong"
    init {
        when (error) {
            is HttpException -> {
                try {
                    val errorJsonString = error.response()?.errorBody()?.string()
                    this.message = JsonParser().parse(errorJsonString)
                        .asJsonObject["message"]
                        .asString
                } catch (e: Exception) {
                    default
                }

            }
            else -> {
                this.message = error.message ?: this.message
            }
        }
    }
}