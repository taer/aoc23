import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

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

data class Point(val x: Int, val y: Int) {
    fun neighbors() =
        setOf(
            copy(x = x + 1),
            copy(x = x - 1),
            copy(y = y + 1),
            copy(y = y - 1),
            copy(x = x - 1, y = y - 1),
            copy(x = x + 1, y = y + 1),
            copy(x = x + 1, y = y - 1),
            copy(x = x - 1, y = y + 1),
        )
}