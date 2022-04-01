package ru.tinkoff.fintech

import java.util.*

class CarService(private val translator: Translator = Translator(Locale("ru"), Locale("en")),
private val currencyCalculator: CurrencyCalculator = CurrencyCalculator()
) {
    companion object {
        private fun Double.format(digits: Int) = "%.${digits}f".format(this)
    }

    fun descriptions(cars: List<Car>): List<String> {
        return cars.asSequence()
            .sortedBy { currencyCalculator.rubleToDollar(it.rublePrice) }
            .map {
                """
                ${translator.translate(it.brand)} ${translator.translate(it.name)}
                Specifications:
                    Type of car body: ${translator.translate(it.bodyType.string)}.
                    Gas mileage: ${it.gasMileage.format(1)} liters per 100 km.
                Price: ${currencyCalculator.rubleToDollar(it.rublePrice).format(2)} dollars.
                """.trimIndent()
            }
            .toList()
    }

    fun groupByBodyType(cars: List<Car>): Map<CarBody, List<Car>> {
        return cars.groupBy { it.bodyType }
    }

    fun takeFirstNames(cars: List<Car>, number: Int = 3, predicate: (Car) -> Boolean): List<String> {
        return cars.asSequence()
            .filter(predicate)
            .take(number)
            .map { it.name }
            .toList()
    }
}