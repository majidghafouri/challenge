/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.getyourguide.challenge.reviewRepo

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.getyourguide.challenge.api.ReviewApi
import com.getyourguide.challenge.vo.Review
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 * See ItemKeyedDataSource
 */
class PageKeyedReviewDataSource(
    private val reviewApi: ReviewApi,
    private val retryExecutor: Executor,
    private val sort: String
) : PageKeyedDataSource<Int, Review>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Review>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Review>
    ) {
        networkState.postValue(NetworkState.LOADING)
        reviewApi.getReviews(
            sort = sort,
            offset = params.key,
            limit = params.requestedLoadSize
        )
            .enqueue(
                object : retrofit2.Callback<ReviewApi.ListingResponse> {
                    override fun onFailure(
                        call: Call<ReviewApi.ListingResponse>,
                        t: Throwable
                    ) {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                    }

                    override fun onResponse(
                        call: Call<ReviewApi.ListingResponse>,
                        response: Response<ReviewApi.ListingResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            val items = data?.reviews?.map { it } ?: emptyList()
                            retry = null
                            val nextKey: Int? =
                                if (data?.pagination?.offset!! >= data?.totalCount!!) null
                                else data?.pagination?.offset + data?.pagination?.limit!!
                            callback.onResult(items, nextKey)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            retry = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(
                                NetworkState.error("error code: ${response.code()}")
                            )
                        }
                    }
                }
            )
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Review>
    ) {
        val request = reviewApi.getReviews(
            limit = 10,
            offset = 0,
            sort = sort
        )
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()

            val items = data?.reviews?.map { it } ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            val nextKey: Int? =
                if (data?.pagination?.offset!! >= data?.totalCount!!) null
                else data?.pagination?.offset + data?.pagination?.limit!!
            callback.onResult(items, null, nextKey)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }
}