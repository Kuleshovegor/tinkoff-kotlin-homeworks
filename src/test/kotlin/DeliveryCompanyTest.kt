import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.tinkoff.fintech.DeliveryCompany
import ru.tinkoff.fintech.transports.Bicycle
import ru.tinkoff.fintech.transports.Car
import kotlin.random.Random

class DeliveryCompanyTest {
    private val car1 = spyk<Car> {
        every { getAmount() } returns 50
    }
    private val car2 = spyk<Car> {
        every { getAmount() } returns 7
    }
    private val bicycle1 = spyk<Bicycle> {
        every { getAmount() } returns 10
    }
    private val bicycle2 = spyk<Bicycle> {
        every { getAmount() } returns -15
    }

    @Test
    fun car1Test() {
        val deliveryCompany = DeliveryCompany(listOf(car1))

        deliveryCompany.addOrder(0, 50, "")
        deliveryCompany.addOrder(0, 51, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 2) { car1.deliverToClients() }
        verify(exactly = 1) { car1.getAmount() }
        Assertions.assertEquals(50, deliveryCompany.amount)
    }

    @Test
    fun car2Test() {
        val deliveryCompany = DeliveryCompany(listOf(car2))

        deliveryCompany.addOrder(0, 47, "")
        deliveryCompany.addOrder(0, 99, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 2) { car2.deliverToClients() }
        verify(exactly = 1) { car2.getAmount() }
        deliveryCompany.addOrder(0, 100, "")
        deliveryCompany.addOrder(0, 1, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 4) { car2.deliverToClients() }
        verify(exactly = 2) { car2.getAmount() }
        Assertions.assertEquals(14, deliveryCompany.amount)
    }

    @Test
    fun bicycle1Test() {

        val deliveryCompany = DeliveryCompany(listOf(bicycle1))

        deliveryCompany.addOrder(0, 50, "")
        deliveryCompany.addOrder(0, 5, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 1) { bicycle1.deliverToClients() }
        verify(exactly = 1) { bicycle1.getAmount() }
        Assertions.assertEquals(10, deliveryCompany.amount)
    }

    @Test
    fun bicycle2Test() {

        val deliveryCompany = DeliveryCompany(listOf(bicycle2))

        deliveryCompany.addOrder(0, 50, "")
        deliveryCompany.addOrder(0, 5, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 1) { bicycle2.deliverToClients() }
        verify(exactly = 1) { bicycle2.getAmount() }
        deliveryCompany.addOrder(0, 10, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 2) { bicycle2.deliverToClients() }
        verify(exactly = 2) { bicycle2.getAmount() }
        Assertions.assertEquals(-30, deliveryCompany.amount)
    }

    @Test
    fun carAndBicycleTest() {
        val deliveryCompany = DeliveryCompany(listOf(car1, bicycle1))

        deliveryCompany.addOrder(0, 50, "")
        deliveryCompany.addOrder(0, 5, "")
        deliveryCompany.deliveryAllOrders()
        verify(exactly = 1) { car1.deliverToClients() }
        verify(exactly = 1) { car1.getAmount() }
        verify(exactly = 1) { bicycle1.deliverToClients()}
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