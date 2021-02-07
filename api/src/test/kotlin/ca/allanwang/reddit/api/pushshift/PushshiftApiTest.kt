package ca.allanwang.reddit.api.pushshift

import ca.allanwang.reddit.api.RedditApiModule
import dagger.Component
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Singleton

class PushshiftApiTest {

    private lateinit var component: PushshiftTestComponent
    private lateinit var api: PushshiftApi

    @BeforeEach
    fun before() {
        component = DaggerPushshiftTestComponent.create()
        api = component.pushshiftApi()
    }

    @Test
    fun rickAstley() {
        runBlocking {
            api.submission {
                ids = setOf("t3_haucpf")
            }.also(::println)
        }
    }

    @Test
    fun myComments() {
        runBlocking {
            api.comment {
                author = "AllanWang"
            }.also(::println)
        }
    }

}

@Singleton
@Component(modules = [RedditApiModule::class])
interface PushshiftTestComponent {
    fun pushshiftApi(): PushshiftApi
}