package ca.allanwang.reddit.api

import ca.allanwang.reddit.api.auth.impl.RedditAuthApiModule
import ca.allanwang.reddit.api.impl.RedditApiProvider
import ca.allanwang.reddit.api.impl.RedditConfigModule
import dagger.Module
import dagger.Provides
import net.dean.jraw.RedditClient

@Module(includes = [RedditConfigModule::class])
object RedditApiModule {
    @Provides
    @JvmStatic
    fun jraw(provider: JrawProvider): RedditClient = provider.client()
}