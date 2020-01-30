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

package com.getyourguide.challenge.api

import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.getyourguide.challenge.vo.Review
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API communication setup
 */
interface ReviewApi {

  @GET("/activities/23776/reviews")
  fun getReviews(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int,
    @Query("sort") sort: String
  ): Call<ListingResponse>

  data class ListingResponse(
      val reviews: List<Review>,
      val totalCount: Long?,
      val averageRating: Double?,
      val pagination: Pagination
  )

  companion object {
    private const val BASE_URL = "https://travelers-api.getyourguide.com"
    fun create(): ReviewApi = create(HttpUrl.parse(BASE_URL)!!)
    fun create(httpUrl: HttpUrl): ReviewApi {
      val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
        Log.d("API", it)
      })
      logger.level = HttpLoggingInterceptor.Level.BASIC
      val client = OkHttpClient.Builder()
          .addNetworkInterceptor(StethoInterceptor())
          .addInterceptor(logger)
          .build()
      return Retrofit.Builder()
          .baseUrl(httpUrl)
          .client(client)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(ReviewApi::class.java)
    }
  }
}