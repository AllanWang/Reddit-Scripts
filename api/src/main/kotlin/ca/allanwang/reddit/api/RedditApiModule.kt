package ca.allanwang.reddit.api

import ca.allanwang.reddit.api.pushshift.PushshiftBaseApi
import ca.allanwang.reddit.api.pushshift.PushshiftProvider
import dagger.Module
import dagger.Provides
import net.dean.jraw.RedditClient
import javax.inject.Singleton

@Module
object RedditApiModule {
    @Provides
    @Singleton
    @JvmStatic
    fun jraw(provider: JrawProvider): RedditClient = provider.client()

    @Provides
    @JvmStatic
    fun pushshift(provider: PushshiftProvider): PushshiftBaseApi =
        provider.createApi()
}