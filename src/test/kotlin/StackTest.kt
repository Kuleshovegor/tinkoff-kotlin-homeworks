import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.random.Random

class StackTest {
    companion object {
        enum class Type {
            PUSH, POP, PEEK
        }

        data class Operation<T>(val type: Type, val value: T?)

        @JvmStatic
        fun source() = listOf(
            Arguments.of(
                listOf(
                    Operation(Type.PUSH, 1),
                    Operation(Type.POP, 1),
                    Operation(Type.POP, null)
                )
            ),
            Arguments.of(
                listOf(
                    Operation(Type.PUSH, 1),
                    Operation(Type.PEEK, 1),
                    Operation(Type.PUSH, 2),
                    Operation(Type.PUSH, 3),
                    Operation(Type.POP, 3),
                    Operation(Type.PEEK, 2),
                    Operation(Type.POP, 2),
                    Operation(Type.POP, 1),
                    Operation(Type.POP, null)
                )
            ),
            Arguments.of(
                listOf(
                    Operation<Int>(Type.POP, null)
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
        val stack = Stack<Int>()
        operations.forEach {
            when (it.type) {
                Type.PUSH -> it.value?.let { it1 -> stack.push(it1) }
                Type.PEEK -> Assertions.assertEquals(it.value, stack.peek())
                Type.POP -> Assertions.assertEquals(it.value, stack.pop())
            }
        }
    }

    @Test
    fun randomTest() {
        val list = MutableList(size = 1000) {0}
        val stack = Stack<Int>()
        var i = -1

        repeat(10000) {
            when (Type.values().random()) {
                Type.PUSH -> {
                    list[++i] = Random.nextInt()
                    stack.push(list[i])
                }
                Type.POP -> {
                    if (i < 0) {
                        Assertions.assertEquals(null, stack.pop())
                    } else {
                        Assertions.assertEquals(list[i], stack.pop())
                        i--
                    }
                }
                Type.PEEK -> {
                    if (i < 0) {
                        Assertions.assertEquals(null, stack.peek())
                    } else {
                        Assertions.assertEquals(list[i], stack.peek())
                    }
                }
            }
        }
    }
}