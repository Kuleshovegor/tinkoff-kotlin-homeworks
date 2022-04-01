package ru.tinkoff.fintech

import java.util.Locale

class Translator(from: Locale, to: Locale) {
    companion object {
        val dictionaries = mapOf(
            (Locale("ru") to Locale("en")) to mapOf(
                "Поло" to "Polo",
                "Фокус" to "Focus",
                "Рио" to "Rio",
                "Гольф" to "Golf",
                "Фольксваген" to "Volkswagen",
                "Форд" to "Ford",
                "Лада" to "Lada",
                "Вольво" to "Volvo",
                "КИА" to "KIA",
                "седан" to "sedan",
                "хетчбек" to "hatchback",
                "универсал" to "wagon"
            )
        )
    }

    private val dictionary = dictionaries[from to to] ?: throw IllegalArgumentException("Dictionary not found")

    fun translate(string: String): String {
        return dictionary[string] ?: string
    }
}