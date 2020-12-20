package ca.allanwang.reddit.api

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {

    @GET("r/{subreddit}/about")
    suspend fun subredditAbout(@Path("subreddit") subreddit: String): Any

    @GET("r/{subreddit}/comments/{id}")
    suspend fun comments(
        @Path("subreddit") subreddit: String,
        @Path("id") id: String,
        @Query("depth") depth: Int? = null,
    ): List<RedditComment>
}

interface RedditConfig {
    val clientId: String
    val clientSecret: String
    var accessToken: String?
    var expiration: Long?

    fun save()
}

// https://www.reddit.com/dev/api#GET_comments_{article}
// https://github.com/mattbdean/JRAW/blob/master/lib/src/main/kotlin/net/dean/jraw/tree/CommentNode.kt
data class RedditComment(
    val kind: String,
    val data: Data
) {
    data class Data(
        val modhash: String,
        val dist: Int,
        val children: List<RedditComment> = emptyList()
    )
}