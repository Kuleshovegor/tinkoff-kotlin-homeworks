class Queue<E> {
    companion object {
        private data class Node<T>(val value: T, var next: Node<T>? = null)
    }

    private var head: Node<E>? = null
    private var tail: Node<E>? = null

    fun element(): E {
        val currentHead = head ?: throw NoSuchElementException("Queue is empty.")

        return currentHead.value
    }

    fun remove(): E {
        val currentHead = head ?: throw NoSuchElementException("Queue is empty.")

        head = currentHead.next

        return currentHead.value
    }

    fun peek(): E? {
        return head?.value
    }

    fun poll(): E? {
        val currentHead = head

        head = currentHead?.next

        return currentHead?.value
    }

    fun offer(obj: E): Boolean {
        val newNode = Node(obj)

        if (head == null) {
            head = newNode
            tail = head
        } else {
            tail!!.next = newNode
            tail = newNode
        }

        return true
    }

}