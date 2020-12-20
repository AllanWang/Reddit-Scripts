package ca.allanwang.reddit.giveaway

import ca.allanwang.reddit.api.RedditApi
import dagger.Component
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Singleton

class HangmanTest {

    private lateinit var hangman: Hangman

    @BeforeEach
    fun before() {
        hangman = DaggerHangmanTestComponent.create().hangman()
    }

    @Test
    fun hangman() {
    }

}

@Singleton
@Component(modules = [HangmanModule::class])
interface HangmanTestComponent {
    fun hangman(): Hangman
}