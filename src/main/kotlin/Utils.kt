import java.math.BigInteger
import java.security.MessageDigest

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

