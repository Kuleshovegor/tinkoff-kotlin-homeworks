import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.tinkoff.fintech.Order
import ru.tinkoff.fintech.transports.Car
import kotlin.random.Random

class AddOrderTest {
    companion object {
        @JvmStatic
        fun arguments() = listOf(
            Arguments.of(listOf<Int>(), listOf<Int>()),
            Arguments.of(listOf(1), listOf(1)),
            Arguments.of(listOf(100), listOf(100)),
            Arguments.of(listOf(101), listOf<Int>()),
            Arguments.of(listOf(50, 50), listOf(50, 50)),
            Arguments.of(listOf(50, 52), listOf(50)),
            Arguments.of(listOf(52, 50), listOf(52)),
            Arguments.of(listOf(50, 52, 50), listOf(50, 50)),
            Arguments.of(listOf(2, 24, 23, 4, 1), listOf(2, 24, 23, 4, 1))
        )
    }

    @ParameterizedTest
    @MethodSource("arguments")
    fun handmadeTests(weights: List<Int>, expectedWeights: List<Int>) {
        val car = Car()
        val expected = mutableListOf<Order>()

        expectedWeights.forEach { expected.add(Order(0,0,it,"")) }

        weights.forEach {
            car.addOrder(Order(0,0,it,""))
        }

        Assertions.assertEquals(expectedWeights, expectedWeights)
    }


    @Test
    fun randomTests() {
        val car = Car()
        repeat(1000) {
            val weights = mutableListOf<Int>()
            val randomWeightSize = Random.nextInt(0, 200)

            repeat(randomWeightSize) {
                val randomWeight = Random.nextInt(1, Int.MAX_VALUE)
                weights.add(randomWeight)
                car.addOrder(Order(0, 0, randomWeight, ""))
            }

            Assertions.assertTrue(car.maxWeight >= car.returnOrders().fold(0) { acc, order -> acc + order.weight })
        }
    }

}