package ca.allanwang.reddit.api

import dagger.Module
import dagger.Provides
import net.dean.jraw.RedditClient

@Module
object RedditApiModule {
    @Provides
    @JvmStatic
    fun jraw(provider: JrawProvider): RedditClient = provider.client()
}