package day18

import point.Point2d
import println
import readInput
import java.lang.RuntimeException
import java.util.PriorityQueue


fun main() {
    fun part1(input: List<String>): Int {
        return 1
    }

    fun part2(input: List<String>): Int {
        return 0
    }


    val testInput = readInput("Day17_test")
    val testtt = """
        112999
        911111
    """.trimIndent()
    val part1a = part1(testtt.lines())
    check(part1a == 7) {
        part1a
    }
    val part1 = part1(testInput)
    check(part1 == 102) {
        part1
    }
//    check(part2(testInput) == 51)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 8901)
    check(part2(input) == 9064)
}

