package ru.tinkoff.fintech

import ru.tinkoff.fintech.exceptions.NotEnergyException
import ru.tinkoff.fintech.exceptions.NotFuelException
import ru.tinkoff.fintech.transports.Bicycle
import ru.tinkoff.fintech.transports.Car
import ru.tinkoff.fintech.transports.Transport

class DeliveryCompany(private val transports: List<Transport>) {
    private val orders = ArrayDeque<Order>()
    private var orderId = 0
    private val maxOrderWeight = transports.maxByOrNull { it.maxWeight }?.maxWeight ?: 0
    private var _amount = 0
    val amount
        get() = _amount

    fun addOrder(price: Int, weight: Int, address: String) {
        check(weight > 0) { "Incorrect weight of order: $weight" }
        check(price >= 0) { "Incorrect price of order: $price" }

        if (weight > maxOrderWeight) {
            System.err.println("Weight of order to $address too large.")

            return
        }

        val order = Order(orderId++, price, weight, address)

        println("Get order: $order")
        orders.add(order)
    }

    private fun getAllActive() {
        for (transport in transports) {
            if (transport is Bicycle) {
                transport.active()
            }
        }
    }

    fun deliveryAllOrders() {
        check(transports.isNotEmpty()) { "There aren't transports." }

        println("Start delivering all orders.")

        while (orders.isNotEmpty()) {
            getAllActive()
            transports.forEach {
                while (orders.isNotEmpty() && it.addOrder(orders.first())) {
                    orders.removeFirst()
                }

                try {
                    it.deliverToClients()
                } catch (e: NotFuelException) {
                    println(e.message)

                    orders.addAll(it.returnOrders())
                    (it as Car).refuel()
                } catch (e: NotEnergyException) {
                    println(e.message)

                    orders.addAll(it.returnOrders())
                    (it as Bicycle).rest()
                }
            }
        }

        transports.forEach { _amount += it.getAmount() }
        println("All orders has been delivered. Current amount: $_amount")
    }
}