package ru.tinkoff.fintech.transports

import ru.tinkoff.fintech.Order

abstract class AbstractTransport(val number: String, override val speed: Int, override val maxWeight: Int) :
    Transport {
    protected var currentGoodsWeight: Int = 0
    protected var currentAmount: Int = 0
    protected val orders = ArrayDeque<Order>()

    override fun addOrder(order: Order): Boolean {
        if (currentGoodsWeight + order.weight <= maxWeight) {
            currentGoodsWeight += order.weight
            orders.add(order)

            println("$this take order ${order.id}")

            return true
        }

        println(
            "$this can't take order ${order.id}." +
                    " current goods weight are $currentGoodsWeight," +
                    " order weight is ${order.weight}," +
                    " but max weight is $maxWeight"
        )

        return false
    }

    override fun getAmount(): Int {
        val amount = currentAmount

        currentAmount = 0

        return amount
    }

    override fun returnOrders(): List<Order> {
        val result = orders.toList()

        orders.clear()
        currentGoodsWeight = 0

        return result
    }
}