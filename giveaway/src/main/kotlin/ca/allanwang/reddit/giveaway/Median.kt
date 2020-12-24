package ca.allanwang.reddit.giveaway

import ca.allanwang.reddit.api.RedditApiModule
import dagger.Module
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Comment
import net.dean.jraw.models.CommentSort
import net.dean.jraw.references.CommentsRequest
import net.dean.jraw.tree.CommentNode
import javax.inject.Inject

class Median @Inject internal constructor(
    private val client: RedditClient
) {
    data class Result(
        val entries: List<MedianEntry>,
        val winner: MedianEntry
    )

    data class MedianEntry(
        val id: String,
        val author: String,
        val number: Long,
        val time: Long
    )

    fun play(submission: String): Result {
        val topComments = client.submission(submission)
            .comments(CommentsRequest(depth = 1, sort = CommentSort.OLD))
            .replies

        fun CommentNode<Comment>.medianEntry(): MedianEntry? {
            val number = subject.body.trim().toLongOrNull() ?: return null
            val date = subject.edited ?: subject.created
            return MedianEntry(
                id = subject.id,
                author = subject.author,
                number = number,
                time = date.time
            )
        }

        val entries = topComments.mapNotNull { it.medianEntry() }.sortedBy { it.number }
        // Not technically the median, but we can check the entry count for even sized participant count
        val medianEntry = entries[entries.size / 2]

        return Result(entries = entries, winner = medianEntry)
    }
}

@Module(includes = [RedditApiModule::class])
interface MedianModule