package ru.tinkoff.fintech

class CurrencyCalculator {
    companion object {
        private const val ONE_DOLLAR_IN_RUBLE = 103.12
    }

    fun rubleToDollar(rubles: Double): Double {
        check(rubles >= 0.0) { "Positive rubles expected." }
        return rubles / ONE_DOLLAR_IN_RUBLE
    }
}