package ca.allanwang.reddit.api

import dagger.Component
import kotlinx.coroutines.runBlocking
import net.dean.jraw.RedditClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Singleton

class RedditApiTest {

    private lateinit var component: RedditTestComponent
    private lateinit var client: RedditClient

    @BeforeEach
    fun before() {
        component = DaggerRedditTestComponent.create()
        client = component.client()
    }

    @Test
    fun subreddit() {
        runBlocking {
            println(component.client().submission("3g1jfi").comments().subject.author)
        }
    }
}


@Singleton
@Component(modules = [RedditApiModule::class])
interface RedditTestComponent {
    fun client(): RedditClient
}