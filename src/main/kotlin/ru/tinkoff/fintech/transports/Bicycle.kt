package ru.tinkoff.fintech.transports

import ru.tinkoff.fintech.Order
import ru.tinkoff.fintech.exceptions.NotEnergyException

class Bicycle(number: String) : AbstractTransport(number, SPEED, MAX_WEIGHT) {
    companion object {
        const val SPEED = 15
        const val MAX_WEIGHT = 15
        const val MAX_ENERGY = 100
    }

    private var driverEnergy = MAX_ENERGY
    private var isAvailable = true

    override fun addOrder(order: Order): Boolean {
        return if (isAvailable) {
            super.addOrder(order)
        } else {
            println("$this can't take order ${order.id}")
            false
        }
    }

    override fun deliverToClients() {
        val notDelivered = mutableListOf<Order>()

        while (orders.isNotEmpty()) {
            val order = orders.first()

            if (driverEnergy < order.weight) {
                notDelivered.add(order)
                orders.removeFirst()
                continue
            }

            orders.removeFirst()
            currentAmount += order.price
            currentGoodsWeight -= order.weight
            driverEnergy -= order.weight

            println("$this delivered order ${order.id} to ${order.address}")
        }

        if (notDelivered.isNotEmpty()) {
            orders.addAll(notDelivered)

            throw NotEnergyException("Need more energy, current energy: $driverEnergy.")
        }

    }

    fun rest() {
        isAvailable = false

        println("$this go rest.")
    }

    fun active() {
        if (!isAvailable) {
            driverEnergy = MAX_ENERGY
            isAvailable = true

            println("$this go active.")
        }
    }

    override fun toString(): String {
        return "ru.tinkoff.fintech.transports.Bicycle \"$number\""
    }
}