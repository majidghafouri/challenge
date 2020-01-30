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

package com.getyourguide.challenge.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.getyourguide.challenge.R
import com.getyourguide.challenge.modules.GlideRequests
import com.getyourguide.challenge.vo.Review

/**
 * A RecyclerView ViewHolder that displays a posts.
 */
class ReviewPostViewHolder(
    view: View,
    private val glide: GlideRequests
) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.title)
    private val message: TextView = view.findViewById(R.id.message)
    private val fullName: TextView = view.findViewById(R.id.fullName)
    private val score: TextView = view.findViewById(R.id.rating_text)
    private val date: TextView = view.findViewById(R.id.created_date)
    private val thumbnail: ImageView = view.findViewById(R.id.photo)
    private val ratingBar: RatingBar = view.findViewById(R.id.rating)
    private var post: Review? = null

    init {
        view.setOnClickListener {
            val direction =
                HomeFragmentDirections.actionHomeToDetails(
                    post!!
                )
            view?.findNavController()
                .navigate(direction)
        }
    }

    fun bind(post: Review?) {
        this.post = post
        title.text = post?.title ?: "loading"
        fullName.text = post?.author?.fullName
        fullName.visibility = if (post?.isAnonymous!!) View.GONE else View.VISIBLE
        score.text = "${post?.rating ?: 0}"
        message.text = post?.message
        ratingBar.rating = post?.rating?.toFloat() ?: 0.0f
        date.text = post?.created
        glide.load(post?.author?.photo)
            .centerCrop()
            .placeholder(R.drawable.noimage)
            .into(thumbnail)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            glide: GlideRequests
        ): ReviewPostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_reviews, parent, false)
            return ReviewPostViewHolder(view, glide)
        }
    }

    fun updateScore(item: Review?) {
        post = item
        score.text = "${item?.rating ?: 0}"
    }
}