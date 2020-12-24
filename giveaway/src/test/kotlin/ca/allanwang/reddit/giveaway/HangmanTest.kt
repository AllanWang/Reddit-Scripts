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

class HangmanTest {

    private lateinit var component: HangmanTestComponent

    private fun HangmanTestComponent.core(word: String): HangmanCore =
        hangmanComponentProvider().get().word(word).build().hangmanCore()

    private fun HangmanTestComponent.extractTest(
        message: String, result: String
    ) {
        val extraction = core("unused").extract(message)
        assertThat(
            "Extraction match",
            extraction,
            equalTo(result.toCharArray().toList())
        )
    }

    private fun HangmanTestComponent.guessTest(
        word: String, guess: String, pass: Boolean = true
    ) {
        val result = core(word).check(guess.toCharArray().toList())
        assertThat("Guess result", result.isEmpty(), equalTo(pass))
    }

    @BeforeEach
    fun before() {
        component = DaggerHangmanTestComponent.create()
    }

    @Test
    @Ignore("Actual run")
    fun hangman() {
        val result = component.hangman()
            .play(
                component.config().submission!!,
                "pomme"
            )
        println(result.guesses.firstOrNull { it.pass })
    }

    @Test
    fun hangmanExtract() {
        component.extractTest("Guess: abcdefg", "abcdefg")
        component.extractTest("guess a, B, a, ba", "ab")
        component.extractTest("guess: qpodi", "qpodi")
    }

    @Test
    fun hangmanBadExtract() {
        listOf("noguessstart", "a-b-c-d", "1234").forEach {
            component.extractTest(it, "")
        }
    }

    @Test
    fun hangmanGuess() {
        component.guessTest("apple", "apel")
        component.guessTest("apple", "bcdfgapel")
        component.guessTest("apple", "abcdefghpl", pass = false)
        component.guessTest("apple", "apl", pass = false)
        component.guessTest(
            "flamingo",
            "rstlenmhoagpicfbdvwykqjxzu",
            pass = false
        )
    }

}


@Singleton
@Component(modules = [HangmanModule::class])
interface HangmanTestComponent {
    fun hangman(): Hangman

    fun config(): RedditConfig

    fun hangmanComponentProvider(): Provider<HangmanComponent.Builder>
}