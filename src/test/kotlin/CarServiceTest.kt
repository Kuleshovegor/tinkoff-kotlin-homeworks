import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.tinkoff.fintech.*

class CarServiceTest {
    companion object {

        @JvmStatic
        fun carsAndDescriptionMock() = listOf(
            Arguments.of(
                listOf<Car>(), listOf<String>()
            ),
            Arguments.of(
                listOf(
                    Car("Поло", "Фольксваген", CarBody.SEDAN, 1_386_000.0, 6.6)
                ),
                listOf(
                    """
                car car
                Specifications:
                    Type of car body: car.
                    Gas mileage: 6,6 liters per 100 km.
                Price: 20,00 dollars.
                """.trimIndent()
                ),
            ),
            Arguments.of(
                listOf(
                    Car("Поло", "Фольксваген", CarBody.SEDAN, 1_386_000.0, 6.6),
                    Car("Фокус", "Форд", CarBody.HATCHBACK, 1_175_000.0, 4.1),
                    Car("2109", "Лада", CarBody.HATCHBACK, 110_000.0, 7.5)
                ),
                listOf(
                    """
                car car
                Specifications:
                    Type of car body: car.
                    Gas mileage: 6,6 liters per 100 km.
                Price: 20,00 dollars.
                """.trimIndent(),
                    """
                car car
                Specifications:
                    Type of car body: car.
                    Gas mileage: 4,1 liters per 100 km.
                Price: 30,00 dollars.
                """.trimIndent(),
                    """
                car car
                Specifications:
                    Type of car body: car.
                    Gas mileage: 7,5 liters per 100 km.
                Price: 40,00 dollars.
                """.trimIndent()
                ),
            )
        )

        @JvmStatic
        fun carsAndDescription() = listOf(
            Arguments.of(
                cars,
                listOf(
                    """
                Lada 2109
                Specifications:
                    Type of car body: hatchback.
                    Gas mileage: 7,5 liters per 100 km.
                Price: 1066,72 dollars.
                """.trimIndent(),
                    """
                KIA Rio
                Specifications:
                    Type of car body: sedan.
                    Gas mileage: 7,4 liters per 100 km.
                Price: 10521,72 dollars.
                """.trimIndent(),
                    """
                Ford Focus
                Specifications:
                    Type of car body: hatchback.
                    Gas mileage: 4,1 liters per 100 km.
                Price: 11394,49 dollars.
                """.trimIndent(),
                    """
                Volkswagen Polo
                Specifications:
                    Type of car body: sedan.
                    Gas mileage: 6,6 liters per 100 km.
                Price: 13440,65 dollars.
                """.trimIndent(),
                    """
                Volvo v60
                Specifications:
                    Type of car body: wagon.
                    Gas mileage: 7,6 liters per 100 km.
                Price: 33931,34 dollars.
                """.trimIndent(),
                    """
                Volkswagen Golf
                Specifications:
                    Type of car body: hatchback.
                    Gas mileage: 6,0 liters per 100 km.
                Price: 35725,37 dollars.
                """.trimIndent()
                ),
            )
        )

        @JvmStatic
        fun groupByBody() =
            listOf(
                Arguments.of(
                    mapOf(
                        CarBody.SEDAN to listOf(
                            Car("Поло", "Фольксваген", CarBody.SEDAN, 1_386_000.0, 6.6)
                        )
                    )
                ),
                Arguments.of(
                    mapOf(
                        CarBody.HATCHBACK to listOf(
                            Car("2109", "Лада", CarBody.HATCHBACK, 110_000.0, 7.5)
                        )
                    )
                ),
                Arguments.of(
                    mapOf(
                        CarBody.WAGON to listOf(
                            Car("v60", "Вольво", CarBody.WAGON, 3_499_000.0, 7.6)
                        )
                    )
                ),
                Arguments.of(
                    mapOf(
                        CarBody.SEDAN to listOf(
                            Car("Поло", "Фольксваген", CarBody.SEDAN, 1_386_000.0, 6.6),
                            Car("Рио", "КИА", CarBody.SEDAN, 1_085_000.0, 7.4),
                        ),
                        CarBody.HATCHBACK to listOf(
                            Car("Фокус", "Форд", CarBody.HATCHBACK, 1_175_000.0, 4.1),
                            Car("2109", "Лада", CarBody.HATCHBACK, 110_000.0, 7.5),
                            Car("Гольф", "Фольксваген", CarBody.HATCHBACK, 3_684_000.0, 6.0)
                        ),
                        CarBody.WAGON to listOf(
                            Car("v60", "Вольво", CarBody.WAGON, 3_499_000.0, 7.6)
                        )
                    )
                )
            )

        @JvmStatic
        fun takeFirstNamesArgs() = listOf(
            Arguments.of(
                listOf<Car>(),
                3,
                { car: Car -> car.bodyType == CarBody.WAGON },
                listOf<Car>()
            ),
            Arguments.of(
                listOf(
                    Car("Поло", "Фольксваген", CarBody.SEDAN, 1_386_000.0, 6.6),
                ),
                3,
                { car: Car -> car.bodyType == CarBody.WAGON },
                listOf<Car>()
            ),
            Arguments.of(
                listOf(
                    Car("Поло", "Фольксваген", CarBody.SEDAN, 1_386_000.0, 6.6),
                ),
                3,
                { car: Car -> car.bodyType == CarBody.SEDAN },
                listOf(
                    "Поло"
                )
            ),
            Arguments.of(
                cars,
                3,
                { car: Car -> car.gasMileage > 6.6 },
                listOf(
                    "2109",
                    "v60",
                    "Рио",
                )
            ),
        )

        private val cars = listOf(
            Car("Поло", "Фольксваген", CarBody.SEDAN, 1_386_000.0, 6.6),
            Car("Фокус", "Форд", CarBody.HATCHBACK, 1_175_000.0, 4.1),
            Car("2109", "Лада", CarBody.HATCHBACK, 110_000.0, 7.5),
            Car("v60", "Вольво", CarBody.WAGON, 3_499_000.0, 7.6),
            Car("Рио", "КИА", CarBody.SEDAN, 1_085_000.0, 7.4),
            Car("Гольф", "Фольксваген", CarBody.HATCHBACK, 3_684_000.0, 6.0),
        )

        val translator = mockk<Translator> {
            every { translate(any()) } returns "car"
        }

        val currencyCalculator = spyk<CurrencyCalculator> {
            every { rubleToDollar(1_386_000.0) } returns 20.0
            every { rubleToDollar(1_175_000.0) } returns 30.0
            every { rubleToDollar(110_000.0) } returns 40.0
        }
    }


    @ParameterizedTest
    @MethodSource("carsAndDescriptionMock")
    fun descriptionsMockTest(cars: List<Car>, descriptions: List<String>) {
        val carService = CarService(translator, currencyCalculator)
        println(CarService().descriptions(Companion.cars))
        Assertions.assertEquals(descriptions, carService.descriptions(cars))
    }


    @ParameterizedTest
    @MethodSource("carsAndDescription")
    fun descriptionsTest(cars: List<Car>, descriptions: List<String>) {
        val carService = CarService()
        Assertions.assertEquals(descriptions, carService.descriptions(cars))
    }

    @Test
    fun groupByBodyTypeEmptyTest() {
        val carService = CarService()
        assertAll({
            Assertions.assertEquals(mapOf<CarBody, Car>(), carService.groupByBodyType(listOf()))
        })
    }

    @ParameterizedTest
    @MethodSource("groupByBody")
    fun groupByBodyTypeTest(expected: Map<CarBody, List<Car>>) {
        val carService = CarService()
        val cars = mutableListOf<Car>()
        expected.values.forEach {
            cars.addAll(it)
        }
        Assertions.assertEquals(expected, carService.groupByBodyType(cars))
    }

    @ParameterizedTest
    @MethodSource("takeFirstNamesArgs")
    fun takeFirstNamesTest(cars: List<Car>, n: Int, predicate: (Car) -> Boolean, expected: List<String>) {
        val carService = CarService()
        Assertions.assertEquals(expected, carService.takeFirstNames(cars, n, predicate))
    }
}