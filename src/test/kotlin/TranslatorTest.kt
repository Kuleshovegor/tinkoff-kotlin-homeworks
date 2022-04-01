import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.tinkoff.fintech.Translator
import java.util.*

class TranslatorTest {
    companion object {
        @JvmStatic
        fun words() = listOf(
            Arguments.of("Фокус", "Focus"),
            Arguments.of("Рио", "Rio",),
            Arguments.of("Лада", "Lada"),
            Arguments.of("v40", "v40"),
            Arguments.of("что-то странное", "что-то странное"),
            Arguments.of("", ""),
        )
    }

    @Test
    fun createTranslatorTest() {
        Assertions.assertDoesNotThrow { Translator(Locale("ru"), Locale("en")) }
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Translator(Locale("en"), Locale("en"))
        }
    }

    @ParameterizedTest
    @MethodSource("words")
    fun translateTest(word: String, expected: String) {
        val translator = Translator(Locale("ru"), Locale("en"))
        Assertions.assertEquals(expected, translator.translate(word))
    }

}