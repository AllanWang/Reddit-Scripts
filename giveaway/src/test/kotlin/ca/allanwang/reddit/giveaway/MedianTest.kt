package ca.allanwang.reddit.giveaway

import ca.allanwang.reddit.api.RedditConfig
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dagger.Component
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.test.Ignore

class MedianTest {

    private lateinit var component: MedianTestComponent

    @BeforeEach
    fun before() {
        component = DaggerMedianTestComponent.create()
    }

    @Test
//    @Ignore("Actual run")
    fun median() {
        val result = component.median()
            .play(component.config().submission!!,)
        println(result.entries.size)
        println(result.entries.map { it.number })
        println(result.winner)
    }


}


@Singleton
@Component(modules = [MedianModule::class])
interface MedianTestComponent {
    fun median(): Median

    fun config(): RedditConfig
}