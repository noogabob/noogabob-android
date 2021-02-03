package com.example.noogabab.domain.repository

import com.example.noogabab.data.api.ApiService
import javax.inject.Inject

class TimelineRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getTimeline(groupId: Int) = apiService.getTimeline(groupId)
}