import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.tinkoff.fintech.CurrencyCalculator

class CurrencyCalculatorTest {
    companion object {
        @JvmStatic
        fun rubleToDollar() = listOf(
            Arguments.of(0.0, 0.0),
            Arguments.of(1.0, 1/103.12),
            Arguments.of(103.12, 1.0),
            Arguments.of(103_120_000_000.0, 1_000_000_000.0),
        )

        @JvmStatic
        fun rubleToDollarError() = listOf(
            Arguments.of(-1.0),
        )
    }

    @ParameterizedTest
    @MethodSource("rubleToDollar")
    fun rubleToDollarTest(value: Double, expected: Double) {
        val currencyCalculator = CurrencyCalculator()
        Assertions.assertEquals(expected, currencyCalculator.rubleToDollar(value))
    }

    @ParameterizedTest
    @MethodSource("rubleToDollarError")
    fun rubleToDollarErrorTest(value: Double) {
        val currencyCalculator = CurrencyCalculator()
        Assertions.assertThrows(IllegalStateException::class.java) {
            currencyCalculator.rubleToDollar(value)
        }
    }
}