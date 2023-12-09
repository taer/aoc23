import java.math.BigInteger
import java.security.MessageDigest
import java.util.LinkedList

/**
 * Reads lines from the given input txt file.
 */

fun readInput(name: String) =  requireNotNull(
    object {}.javaClass.getResourceAsStream ("/$name.txt")
) {
    "$name not found"
}.reader().readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Iterable<Int>.product() = fold(1L){x,y-> x*y}
fun Iterable<Long>.productLong() = fold(1L){x,y-> x*y}

fun String.splitWhiteToInt()= split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
fun String.splitWhiteToLong()= split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

fun <T> Sequence<T>.split(predicate: (T) -> Boolean): Sequence<List<T>> {
    val iterator = this.iterator()
    val buffer = mutableListOf<T>()

    return sequence {
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (predicate.invoke(element)) {
                yield(buffer.toList())
                buffer.clear()
            } else {
                buffer.add(element)
            }
        }
        if (buffer.isNotEmpty()) {
            yield(buffer.toList())
        }
    }
}

inline fun <T> Iterable<T>.sumOfIndexed(selector: (index: Int, T) -> Int): Int {
    var index = 0
    var sum = 0
    for (element in this) {
        sum += selector(index++, element)
    }
    return sum
}

fun <T> List<T>.toLinkedList() = LinkedList(this)
