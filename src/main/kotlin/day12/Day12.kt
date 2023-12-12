package day12

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import println
import readInput

private fun countInner(springs: String, buckets: List<Int>): Long {

    val emptySpring = springs.trim('.').isEmpty()
    if(emptySpring){
        return if(buckets.isEmpty()) 1 else 0
    }

    val droppedFirstLetter = springs.drop(1)
    val firstLetter = springs[0]
    if(firstLetter == '.'){
        return count(droppedFirstLetter, buckets)
    }
    if(firstLetter == '?'){
        val dotFirst = count(".$droppedFirstLetter", buckets)
        val hashFirst = count("#$droppedFirstLetter", buckets)
        return dotFirst + hashFirst
    }
    if(firstLetter == '#'){
        if(buckets.isEmpty())return 0
        val amount = buckets.first()
        if(amount > springs.length)
            return 0
        val hashes = springs.take(amount).all{ it == '#' || it == '?' }
        if(hashes){
            val nextBuckets = buckets.drop(1)
            if(springs.length==amount){
                return if(nextBuckets.isEmpty()) 1 else 0
            }
            val nextAfterPairing = springs.getOrNull(amount) //next char
            return if(nextAfterPairing==null && nextBuckets.isEmpty()){
                1
            }else if(nextAfterPairing=='#'){
                0
            }else if(nextAfterPairing=='?'){
                count("." + springs.drop(amount+1), nextBuckets)
            }else{
                count(springs.drop(amount+1), nextBuckets)
            }
        }
    }
    return 0

}
private val cache = mutableMapOf<Pair<String,List<Int>>, Long>()
private fun count(springs: String, buckets: List<Int>): Long {
    val cacheKey = springs to buckets
    val cached = cache[cacheKey]
    if(cached !=null) {
        return cached
    }

    return countInner(springs,buckets).also {
        cache[cacheKey] = it
    }
}
fun main() {


    fun permute2(it: String): Flow<String> = flow<String>{
        if(!it.contains("?")) {
            emit(it)
        }else{
            emitAll(permute2(it.replaceFirst("?", ".")))
            emitAll(permute2(it.replaceFirst("?", "#")))
        }
    }

    val delimiters = "\\.+".toRegex()
    fun matchesBucket(springs: String, buckets: List<Int>): Boolean {
        val split = springs.split(delimiters)
            .filter { it.isNotEmpty() }
            .map { it.length }
        return buckets == split
    }

    fun doWork(line: String): Int {
        val (springs, bucketStr ) = line.split(" ", limit = 2)
        val buckets = bucketStr.split(",").map { it.toInt() }

        val permute = permute2(springs)

        return runBlocking {
            permute.count {
                matchesBucket(it, buckets)
            }
        }
    }


    fun doWork2(line: String): Long {
        val (springs, bucketStr ) = line.split(" ", limit = 2)
        val buckets = bucketStr.split(",").map { it.toInt() }

        val count = count(springs.trim(('.')), buckets)
        return count

    }

    fun part1(input: List<String>): Long {
        return input.sumOf {
            doWork2(it)
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf {
            val (a,b) = it.split(" ")
            val expanded = buildString {
                append(List(5) { a }.joinToString("?"))
                append(" ")
                append(List(5) { b }.joinToString(","))
            }
            doWork2(expanded)
        }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 7169L)
    check(part2(input) == 1738259948652L)
}

