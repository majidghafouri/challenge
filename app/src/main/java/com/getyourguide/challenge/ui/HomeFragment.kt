/*
 * Copyright 2018, The Android Open Source Project
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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.getyourguide.challenge.R
import com.getyourguide.challenge.ServiceLocator
import com.getyourguide.challenge.adapter.ReviewAdapter
import com.getyourguide.challenge.modules.GlideApp
import com.getyourguide.challenge.reviewRepo.NetworkState
import com.getyourguide.challenge.reviewRepo.ReviewPostRepository
import com.getyourguide.challenge.vo.Review
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Shows the main title screen.
 */
class HomeFragment : Fragment() {
    companion object {
        private var fabExpanded = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_home, container, false
        )
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSwipeToRefresh()
        initSort()
    }

    private fun initSort() {
        closeSubMenusFab()
        fab.setOnClickListener { view ->
            if (fabExpanded) {
                closeSubMenusFab()
            } else {
                openSubMenusFab()
            }
        }
        asc.setOnClickListener { view ->
            sortAsc()
            closeSubMenusFab()
        }
        desc.setOnClickListener { view ->
            sortDesc()
            closeSubMenusFab()
        }
    }

    private fun sortDesc() {

    }

    private fun sortAsc() {

    }

    //closes FAB submenus
    private fun closeSubMenusFab() {
        layoutFabSave.visibility = View.INVISIBLE
        fabExpanded = false

    }

    //Opens FAB submenus
    private fun openSubMenusFab() {
        layoutFabSave.visibility = View.VISIBLE
        fabExpanded = true
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = ReviewAdapter(glide) {
            model.retry()
        }
        list.adapter = adapter
        model.posts.observe(this, Observer<PagedList<Review>> {
            adapter.submitList(it) {
                // Workaround for an issue where RecyclerView incorrectly uses the loading / spinner
                // item added to the end of the list as an anchor during initial load.
                val layoutManager = (list.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    list.scrollToPosition(position)
                }
            }
        })
        model.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {
        model.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
    }

    private val model: ReviewViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val repoType = ReviewPostRepository.Type.values()[0]
                val repo = ServiceLocator.instance(context = requireContext())
                    .getRepository(repoType)
                @Suppress("UNCHECKED_CAST")
                return ReviewViewModel(repo, handle) as T
            }
        }
    }

}
