package day01

import println
import readInput

fun main() {
    val digitMap = buildMap {
        (1..9).forEach {
            put(it.toString(),it.toString())
        }
        putAll(
            mapOf(
                "one" to "1",
                "two" to "2",
                "three" to "3",
                "four" to "4",
                "five" to "5",
                "six" to "6",
                "seven" to "7",
                "eight" to "8",
                "nine" to "9",
            )
        )
    }

    fun part1(input: List<String>) = input.sumOf {
        val a = it.first { it.isDigit() }
        val b = it.last { it.isDigit() }
        "$a$b".toInt()
    }

    fun part2(input: List<String>) = input.sumOf {
        val a = digitMap[it.findAnyOf(digitMap.keys)?.second]
        val b = digitMap[it.findLastAnyOf(digitMap.keys)?.second]
        "$a$b".toInt()
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val part2Input = readInput("Day01_test_part2")
    check(part2(part2Input) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
