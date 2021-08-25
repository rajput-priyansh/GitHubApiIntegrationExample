package com.vibs.dynamicview.api

import com.vibs.githubapidemo.api.ApiClient
import com.vibs.githubapidemo.api.ApiInterface


class ApiRepository {


    fun getGlobalInstance(): ApiInterface {
        return ApiClient().retroClient
    }
}