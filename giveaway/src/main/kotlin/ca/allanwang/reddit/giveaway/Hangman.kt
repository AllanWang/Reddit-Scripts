package ca.allanwang.reddit.giveaway

import ca.allanwang.reddit.api.RedditApi
import ca.allanwang.reddit.api.impl.RedditApiModule
import dagger.Module
import javax.inject.Inject

class Hangman @Inject internal constructor(
    private val api: RedditApi
) {

    fun extract(comment: String): Set<Char> {
        TODO()
    }

}

@Module(includes = [RedditApiModule::class])
interface HangmanModule