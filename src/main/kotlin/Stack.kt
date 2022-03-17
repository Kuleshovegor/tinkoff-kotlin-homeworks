class Stack<E> {
    companion object {
        private data class Node<T>(val value: T, var next: Node<T>? = null)
    }

    private var head: Node<E>? = null

    fun push(obj: E) {
        head = if (head == null) {
            Node(obj)
        } else {
            Node(obj, head)
        }
    }

    fun pop(): E? {
        return if (head == null) {
            null
        } else {
            val result = head?.value

            head = head?.next

            result
        }
    }

    fun peek(): E? {
        return head?.value
    }
}