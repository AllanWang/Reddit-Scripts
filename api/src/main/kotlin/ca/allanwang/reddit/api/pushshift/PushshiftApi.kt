package ca.allanwang.reddit.api.pushshift

import java.util.*
import javax.inject.Inject
import kotlin.math.min

class PushshiftApi @Inject internal constructor(
    private val baseApi: PushshiftBaseApi
) {

    class Config {
        var q: String? = null
        var ids: Set<String> = emptySet()
        val size: Int? = null
        var sort: Sort = Sort.DESC
        var author: String? = null
        var subreddit: String? = null
        var after: Long? = null
        var before: Long? = null

        enum class Sort { ASC, DESC }
        enum class SortType { SCORE, NUM_COMMENTS, CREATED_UTC }
    }

    suspend fun comment(configure: Config.() -> Unit = {}): PushshiftResponse {
        val config = Config().apply(configure)
        return baseApi.comment(
            q = config.q,
            ids = config.ids.commaDelimited(),
            size = config.size?.let { min(500, it) },
            sort = config.sort.name.toLowerCase(Locale.US),
            author = config.author,
            subreddit = config.subreddit,
            after = config.after,
            before = config.before,
        )
    }

    suspend fun submission(configure: Config.() -> Unit = {}): PushshiftResponse {
        val config = Config().apply(configure)
        return baseApi.submission(
            q = config.q,
            ids = config.ids.commaDelimited(),
            size = config.size?.let { min(500, it) },
            sort = config.sort.name.toLowerCase(Locale.US),
            author = config.author,
            subreddit = config.subreddit,
            after = config.after,
            before = config.before,
        )
    }


    private fun Set<String>.commaDelimited(): String? =
        takeIf { it.isNotEmpty() }?.joinToString(",")
}