package ca.allanwang.reddit.giveaway

import ca.allanwang.reddit.api.RedditApiModule
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Comment
import net.dean.jraw.models.CommentSort
import net.dean.jraw.references.CommentsRequest
import net.dean.jraw.tree.CommentNode
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Qualifier

/**
 * Comment must contain a guess in the form of:
 * `guess: abcdefghijklmnop`
 * (case insensitive)
 *
 * Guesses are compared one letter at a time to the word, with 6 lives total.
 */
class Hangman @Inject internal constructor(
    private val client: RedditClient,
    private val hangmanCoreProvider: Provider<HangmanComponent.Builder>
) {

    data class Result(
        val guesses: List<GuessResult>
    )

    data class GuessResult(
        val id: String,
        val author: String,
        val message: String,
        val guess: List<Char>,
        val remaining: Int,
        val time: Long
    ) {
        val pass: Boolean get() = remaining == 0
    }

    /**
     * Get results for all top level comments of the provided [submission]
     */
    suspend fun play(submission: String, word: String): Result {
        val component =
            hangmanCoreProvider.get().word(word).build()
        val core = component.hangmanCore()

        val topComments = client.submission(submission)
            .comments(CommentsRequest(depth = 1, sort = CommentSort.OLD))
            .replies

        fun CommentNode<Comment>.guessResult(): GuessResult {
            val message = subject.body
            val guess = core.extract(message)
            val remaining = core.check(guess)
            val date = subject.edited ?: subject.created
            return GuessResult(
                id = subject.id,
                author = subject.author,
                message = message,
                guess = guess,
                remaining = remaining.size,
                time = date.time
            )
        }

        return Result(topComments.map { it.guessResult() })
    }

}

class HangmanCore @Inject internal constructor(
    @HangmanWord private val word: String
) {

    companion object {
        private const val GUESSES = 6
        private val guessParser = Regex("guess:?\\s+((?:[a-z](?:,\\s)?)+)")
    }

    private val wordCharSet = word.toCharArray().toSet()

    init {
        if (wordCharSet.any { !it.isLetter() })
            throw IllegalArgumentException("Invalid word '$word'. Must only contain letters.")
    }

    /**
     * Get character guesses of a message. If invalid, an empty list is returned
     */
    fun extract(message: String): List<Char> {
        val guess =
            guessParser.find(message.toLowerCase(Locale.US))
                ?.groupValues?.getOrNull(1)
                ?.takeIf { it.isNotEmpty() } ?: return emptyList()
        return guess.toCharArray().filter { it.isLetter() }.distinct()
    }

    /**
     * Check guesses against the word, returning the remaining characters left in the word.
     * If a check is successful, the return set should be empty
     */
    fun check(guess: List<Char>): Set<Char> {
        val remaining = wordCharSet.toMutableSet()
        var lives = GUESSES
        guess.forEach {
            if (!remaining.remove(it)) {
                lives--
            }
            if (remaining.isEmpty()) return remaining
            if (lives <= 0) return remaining
        }
        return remaining
    }

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HangmanWord

@GiveawayScope
@Subcomponent
interface HangmanComponent {

    @GiveawayScope
    fun hangmanCore(): HangmanCore

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun word(@HangmanWord word: String): Builder

        fun build(): HangmanComponent
    }
}

@Module(
    includes = [RedditApiModule::class],
    subcomponents = [HangmanComponent::class]
)
interface HangmanModule