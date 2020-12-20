package ca.allanwang.reddit.api

import ca.allanwang.reddit.api.impl.RedditApiModule
import ca.allanwang.reddit.api.impl.RedditApiProvider
import dagger.Component
import kotlinx.coroutines.runBlocking
import net.dean.jraw.RedditClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Singleton

class RedditApiTest {

    private lateinit var component: RedditTestComponent
    private lateinit var api: RedditApi

    @BeforeEach
    fun before() {
        component = DaggerRedditTestComponent.create()
        api = component.api()
    }

    @Test
    fun saveNewToken() {
        runBlocking {
            component.apiProvider().newToken()
        }
    }

    @Test
    fun subreddit() {
        runBlocking {
            println(component.client().submission("3g1jfi").comments().firstOrNull())
//            println(api.comments("funny", "3g1jfi"))
        }
    }
}


@Singleton
@Component(modules = [RedditApiModule::class])
interface RedditTestComponent {
    fun api(): RedditApi

    fun apiProvider(): RedditApiProvider

    fun client(): RedditClient
}