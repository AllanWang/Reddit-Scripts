package ca.allanwang.reddit.api

import net.dean.jraw.RedditClient
import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.OAuthHelper
import java.util.*
import javax.inject.Inject

class JrawProvider @Inject internal constructor(
    private val config: RedditConfig
) {
    fun client(): RedditClient {
        val uuid = config.uuid ?: UUID.randomUUID().also {
            config.uuid = it
            config.save()
        }

        val creds = Credentials.userless(
            config.clientId,
            config.clientSecret,
            uuid
        )

        val userAgent =
            UserAgent("bot", "ca.allanwang.reddit", "1.0.0", uuid.toString())

        return OAuthHelper.automatic(OkHttpNetworkAdapter(userAgent), creds)
    }
}