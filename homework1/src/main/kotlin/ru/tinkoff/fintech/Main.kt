package ru.tinkoff.fintech

import ru.tinkoff.fintech.transports.Bicycle
import ru.tinkoff.fintech.transports.Car
import ru.tinkoff.fintech.transports.Transport

fun main() {
    val bigOrder = Order(1, 100, 90, "ул. Ленина, д. 3")
    val miniOrder = Order(2, 15, 10, "ул. Ленина, д. 33")

    println(bigOrder)
    println(miniOrder)

    val bike = Bicycle("a123ke")

    println(bike)

    bike.addOrder(miniOrder)
    bike.addOrder(bigOrder)

    bike.deliverToClients()

    println("Amount:${bike.getAmount()}")

    bike.rest()
    bike.active()

    val car = Car("a342ee")

    println(car)

    car.addOrder(miniOrder)
    car.addOrder(bigOrder)

    car.deliverToClients()

    println("Amount:${car.getAmount()}")

    car.refuel()

    val transports = listOf<Transport>(bike, car)

    transports.forEach {
        it.addOrder(bigOrder)
        it.deliverToClients()
        if (it is Bicycle) {
            it.rest()
            it.active()
        } else if (it is Car) {
            it.refuel()
        }
    }

    val deliveryCompany = DeliveryCompany(transports)

    deliveryCompany.addOrder(200, 55, "ул. Ленина, д. 3")
    deliveryCompany.addOrder(16, 10, "ул. Пушкина, д. 33")

    deliveryCompany.deliveryAllOrders()

    for (i in 1..20) {
        deliveryCompany.addOrder(16, 10, "ул. Пушкина, д. 33")
    }
    deliveryCompany.deliveryAllOrders()
}