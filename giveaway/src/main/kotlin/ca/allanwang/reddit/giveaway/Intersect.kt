package ca.allanwang.reddit.giveaway

import ca.allanwang.reddit.api.RedditApiModule
import ca.allanwang.reddit.api.pushshift.PushshiftApi
import ca.allanwang.reddit.api.pushshift.PushshiftResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import javax.inject.Inject

/**
 * Retrieves comment data between two users
 */
class Intersect @Inject internal constructor(
    private val api: PushshiftApi
) {

    private val jsonAdapter: JsonAdapter<Map<*, *>>

    init {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())
        jsonAdapter = moshi.build().adapter(Map::class.java).indent("  ")
    }

    private fun PushshiftApi.Config.defaultConfig() {
        size = 500
    }

    suspend fun intersect(
        user1: String,
        user2: String,
        configure: PushshiftApi.Config.() -> Unit = {}
    ) {
        val user1Response =
            api.comment { defaultConfig(); configure(); author = user1 }
        val user2Response =
            api.comment { defaultConfig(); configure(); author = user2 }

        val user1Info = UserInfo(user1, user1Response)
        val user2Info = UserInfo(user2, user2Response)

        val result = user1Info.intersect(user2Info)

        println(buildString {
            append(user1Info.debugString())
            append("\n\n\n")
            append(user2Info.debugString())
            append("\n\n\n")
            append(jsonAdapter.toJson(result))
        })
    }

    internal class UserInfo(
        val author: String,
        val response: PushshiftResponse
    ) {
        val data: Map<String, List<PushshiftResponse.Data>> =
            response.data.asSequence().filter { it.link_id != null }
                .groupBy { it.link_id!! }

        fun intersect(other: UserInfo): Map<String, List<PushshiftResponse.Data>> {
            val submissionIntersect = data.keys.intersect(other.data.keys)
            return (data.filterKeys { it in submissionIntersect }.asSequence() +
                    other.data.filterKeys { it in submissionIntersect }
                        .asSequence())
                .groupBy({ it.key }, { it.value })
                .mapValues { (_, values) -> values.flatten() }
                // Show oldest interactins first
                .toList()
                .sortedBy { pair ->
                    pair.second.mapNotNull { it.created_utc }.minOrNull()
                }
                .toMap()

        }

        fun debugString(): String = buildString {
            appendLine(author)

            append("Size: ")
            append(response.data.size)
            appendLine()

            append("Submissions: ")
            append(data.keys.joinToString(","))
            appendLine()
        }
    }
}

@Module(includes = [RedditApiModule::class])
interface IntersectModule