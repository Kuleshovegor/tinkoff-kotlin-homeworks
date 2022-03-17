import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.random.Random

class QueueTest {
    companion object {
        enum class Type {
            ELEMENT, REMOVE, PEEK, POLL, OFFER
        }

        data class Operation<T>(val type: Type, val value: T?)

        @JvmStatic
        fun source() = listOf(
            Arguments.of(
                listOf(
                    Operation(Type.OFFER, 1),
                    Operation(Type.POLL, 1),
                    Operation(Type.POLL, null)
                )
            ),
            Arguments.of(
                listOf(
                    Operation(Type.OFFER, 1),
                    Operation(Type.OFFER, 2),
                    Operation(Type.OFFER, 3),
                    Operation(Type.REMOVE, 1),
                    Operation(Type.POLL, 2),
                    Operation(Type.REMOVE, 3),
                    Operation(Type.POLL, null)
                )
            ),
            Arguments.of(
                listOf(
                    Operation<Int>(Type.POLL, null)
                )
            ),
            Arguments.of(
                listOf(
                    Operation<Int>(Type.PEEK, null)
                )
            )
        )
    }

    @ParameterizedTest
    @MethodSource("source")
    fun test(operations: List<Operation<Int>>) {
        val queue = Queue<Int>()
        operations.forEach {
            when (it.type) {
                Type.OFFER -> it.value?.let { it1 -> Assertions.assertTrue(queue.offer(it1)) }
                Type.ELEMENT -> Assertions.assertEquals(it.value, queue.element())
                Type.REMOVE -> Assertions.assertEquals(it.value, queue.remove())
                Type.PEEK -> Assertions.assertEquals(it.value, queue.peek())
                Type.POLL -> Assertions.assertEquals(it.value, queue.poll())
            }
        }
    }

    @Test
    fun randomTest() {
        val list = MutableList(size = 10000) { 0 }
        val queue = Queue<Int>()
        var head = 0
        var tail = -1

        repeat(10000) {
            when (Type.values().random()) {
                Type.OFFER -> {
                    list[++tail] = Random.nextInt()
                    Assertions.assertTrue(queue.offer(list[tail]))
                }
                Type.ELEMENT -> {
                    if (tail < head) {
                        Assertions.assertThrows(NoSuchElementException::class.java) {
                            queue.element()
                        }
                    } else {
                        Assertions.assertEquals(list[head], queue.element())
                    }
                }
                Type.REMOVE -> {
                    if (tail < head) {
                        Assertions.assertThrows(NoSuchElementException::class.java) {
                            queue.remove()
                        }
                    } else {
                        Assertions.assertEquals(list[head++], queue.remove())
                    }
                }
                Type.PEEK -> {
                    if (tail < head) {
                        Assertions.assertEquals(null, queue.peek())
                    } else {
                        Assertions.assertEquals(list[head], queue.peek())
                    }
                }
                Type.POLL -> {
                    if (tail < head) {
                        Assertions.assertEquals(null, queue.poll())
                    } else {
                        Assertions.assertEquals(list[head++], queue.poll())
                    }
                }
            }
        }
    }
}