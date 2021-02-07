package ca.allanwang.reddit.giveaway

import ca.allanwang.reddit.api.pushshift.PushshiftApi
import dagger.Component
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Singleton

class IntersectTest {

    private lateinit var component: IntersectTestComponent
    private lateinit var intersect: Intersect

    @BeforeEach
    fun before() {
        component = DaggerIntersectTestComponent.create()
        intersect = component.intersect()
    }

    @Test
    fun intersect() {
        runBlocking {
            intersect.intersect("user1", "user2") {
//                after = 1577836800L // 2020/01/01
//                sort = PushshiftApi.Config.Sort.ASC
            }
        }
    }
}

@Singleton
@Component(modules = [IntersectModule::class])
interface IntersectTestComponent {
    fun intersect(): Intersect
}