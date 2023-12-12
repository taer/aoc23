package day12

import println
import readInput

fun main() {


    fun permute(it: String): List<String>{
        if(!it.contains("?")) return listOf(it)

        val replaceFirst = it.replaceFirst("?", ".")
        val replaceFirst2 = it.replaceFirst("?", "#")
        return listOf(permute(replaceFirst), permute(replaceFirst2)).flatten()
    }
    
    val delimiters = "\\.+".toRegex()
    fun matchesChecksun(springs: String, buckets: List<Int>): Boolean {
        val split = springs.split(delimiters)
            .filter { it.isNotEmpty() }
            .map { it.length }
        return buckets == split
    }

    fun doWork(line: String): Int {
        val (springs, bucketStr ) = line.split(" ", limit = 2)
        val buckets = bucketStr.split(",").map { it.toInt() }

        val permute = permute(springs)

        return permute.count {
            matchesChecksun(it, buckets)
        }

    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            doWork(it)
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val (a,b) = it.split(" ")
            val expando = "$a?$a?$a?$a?$a $b,$b,$b,$b,$b"
            doWork(expando)
        }
    }

    val testInput = readInput("Day12_test")
    part1(testInput).println()
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 7169)
//    check(part2(input) == 544723432977L)
}

