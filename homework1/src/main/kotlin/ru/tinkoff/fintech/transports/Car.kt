package ru.tinkoff.fintech.transports

import ru.tinkoff.fintech.Order
import ru.tinkoff.fintech.exceptions.NotFuelException

class Car(number: String) : AbstractTransport(number, SPEED, MAX_WEIGHT) {
    companion object {
        const val SPEED = 60
        const val MAX_WEIGHT = 100
        const val MAX_FUEL = 100
        const val FUEL_FOR_ONE_DELIVERY = 10
    }

    private var fuel = MAX_FUEL

    override fun addOrder(order: Order): Boolean {
        if (orders.size >= MAX_FUEL / FUEL_FOR_ONE_DELIVERY) {
            println("$this can't take order ${order.id}")

            return false
        }

        return super.addOrder(order)
    }

    override fun deliverToClients() {
        while (orders.isNotEmpty()) {
            val order = orders.first()

            if (fuel < FUEL_FOR_ONE_DELIVERY) {
                throw NotFuelException("Need $FUEL_FOR_ONE_DELIVERY fuel, but found $fuel.")
            }

            orders.removeFirst()
            currentAmount += order.price
            fuel -= FUEL_FOR_ONE_DELIVERY
            currentGoodsWeight -= order.weight

            println("$this delivered order ${order.id} to ${order.address}")
        }
    }

    fun refuel() {
        fuel = MAX_FUEL

        println("$this refueled.")
    }

    override fun toString(): String {
        return "ru.tinkoff.fintech.transports.Car \"$number\""
    }
}