package ru.tinkoff.fintech.transports

import ru.tinkoff.fintech.Order

interface Transport {
    val speed: Int
    val maxWeight: Int

    fun addOrder(order: Order): Boolean
    fun deliverToClients()
    fun getAmount(): Int
    fun returnOrders(): List<Order>
}