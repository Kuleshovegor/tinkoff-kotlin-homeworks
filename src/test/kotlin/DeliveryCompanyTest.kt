import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.tinkoff.fintech.DeliveryCompany
import ru.tinkoff.fintech.transports.Bicycle
import ru.tinkoff.fintech.transports.Car
import kotlin.random.Random

class DeliveryCompanyTest {
    private val car = spyk<Car> {
        every { getAmount() } returns 50
    }
    private val bicycle = spyk<Bicycle> {
        every { getAmount() } returns 10
    }

    @Test
    fun carTest() {
        val deliveryCompany = DeliveryCompany(listOf(car))

        deliveryCompany.addOrder(0, 50, "")
        deliveryCompany.addOrder(0, 51, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 2) { car.deliverToClients() }
        verify(exactly = 1) { car.getAmount() }
        Assertions.assertEquals(50, deliveryCompany.amount)
    }

    @Test
    fun bicycleTest() {

        val deliveryCompany = DeliveryCompany(listOf(bicycle))

        deliveryCompany.addOrder(0, 50, "")
        deliveryCompany.addOrder(0, 5, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 1) { bicycle.deliverToClients() }
        verify(exactly = 1) { bicycle.getAmount() }
        Assertions.assertEquals(10, deliveryCompany.amount)
    }

    @Test
    fun carAndBicycleTest() {
        val deliveryCompany = DeliveryCompany(listOf(car, bicycle))

        deliveryCompany.addOrder(0, 50, "")
        deliveryCompany.addOrder(0, 5, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 1) { car.deliverToClients() }
        verify(exactly = 1) { car.getAmount() }
        verify(exactly = 1) { bicycle.deliverToClients()}
        Assertions.assertEquals(60, deliveryCompany.amount)
    }

    @Test
    fun randomTest() {
        val deliveryCompany = DeliveryCompany(listOf(Car(), Bicycle()))
        var summaryPrice = 0

        repeat(Random.nextInt(1, 200)) {
            val price = Random.nextInt(1, 10000)
            val weight = Random.nextInt(1, 100)
            deliveryCompany.addOrder(price, weight,"")
            summaryPrice += price
        }

        deliveryCompany.deliveryAllOrders()

        Assertions.assertEquals(summaryPrice, deliveryCompany.amount)
    }
}