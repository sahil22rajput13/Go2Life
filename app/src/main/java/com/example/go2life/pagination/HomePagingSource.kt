package com.example.go2life.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.go2life.model.homeData.home.Body
import com.example.go2life.model.homeData.home.HomePramModel
import com.example.go2life.network.GetApi

class HomePagingSource(private val limit: String) : PagingSource<Int, Body>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Body> {
        return try {
            val position = params.key ?: 0
            val response = GetApi.api.onHome(HomePramModel(limit, position.toString()))

            val nextKey = if (response.body()?.body?.isEmpty() == true) null else position + 10
            LoadResult.Page(response.body()?.body ?: emptyList(), null, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Body>): Int? {
        val anchorPosition = state.anchorPosition
        return anchorPosition?.let { anchor ->
            val closestPage = state.closestPageToPosition(anchor)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}
