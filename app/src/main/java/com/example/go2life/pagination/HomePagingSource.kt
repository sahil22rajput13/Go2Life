package com.example.go2life.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.go2life.model.home.Body
import com.example.go2life.model.home.HomePramModel
import com.example.go2life.network.GetApi

class HomePagingSource(private val limit: String) : PagingSource<Int, Body>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Body> {
        return try {
            val currentPage = params.key ?: 0
            val response = GetApi.api.onHome(
                HomePramModel(limit, currentPage.toString())
            )
            val data = response.body()?.body ?: emptyList()

            val prevKey = if (currentPage > 0) currentPage - 1 else null
            val nextKey = if (data.isNotEmpty()) currentPage + 1 else null

            LoadResult.Page(data, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Body>): Int? {
        // Calculate the refresh key based on the anchor position and paging state
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
