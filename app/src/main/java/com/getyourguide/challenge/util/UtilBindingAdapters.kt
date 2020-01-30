/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.getyourguide.challenge.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.getyourguide.challenge.R

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(
    view: ImageView,
    imageUrl: String?
) {

    val options: RequestOptions = RequestOptions()
        .error(R.drawable.noimage)
    Glide.with(view.context)
        .load(imageUrl)
        .apply(options)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("visible")
fun bindVisible(
    view: TextView,
    type: String?
) {

    if (type.isNullOrEmpty())
        view.visibility = View.GONE
    else
        view.visibility = View.VISIBLE
}
