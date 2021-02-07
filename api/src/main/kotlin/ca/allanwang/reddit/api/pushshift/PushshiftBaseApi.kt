package ca.allanwang.reddit.api.pushshift

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

// See https://github.com/pushshift/api
interface PushshiftBaseApi {
    @GET("comment")
    suspend fun comment(
        // Search term
        @Query("q") q: String?,
        // Comma delimted base36 ids
        @Query("ids") ids: String?,
        // Default 25, max 500
        @Query("size") size: Int?,
        @Query("sort") sort: String?,
        @Query("author") author: String?,
        @Query("subreddit") subreddit: String?,
        @Query("after") after: Long?,
        @Query("before") before: Long?,
    ): PushshiftResponse

    @GET("submission")
    suspend fun submission(
        // Search term
        @Query("q") q: String?,
        // Comma delimted base36 ids
        @Query("ids") ids: String?,
        // Default 25, max 500
        @Query("size") size: Int?,
        @Query("sort") sort: String?,
        @Query("author") author: String?,
        @Query("subreddit") subreddit: String?,
        @Query("after") after: Long?,
        @Query("before") before: Long?,
    ): PushshiftResponse
}

data class PushshiftResponse(
    val data: List<Data>,
    val metadata: MetaData?
) {
    data class Data(
        val author: String?,
        val body: String?,
        val created_utc: Long?,
        val id: String,
        val is_submitter: Boolean?,
        val permalink: String,
        val link_id: String?,
        val parent_id: String?,
        val score: Int,
        val subreddit: String,
        val subreddit_id: String,
    )

    data class MetaData(
        val results_returned: Int,
        val size: Int,
        val total_results: Int,
        val version: String,
    )
}