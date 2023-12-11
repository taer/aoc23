package day11

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return 1
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 4)
//    check(part2(testInput) == 4)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
//    check(part1(input) == 6864)
//    check(part2(input) == 349)
}

