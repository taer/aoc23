package day17

import println
import readInput


fun main() {

    fun part1(input: List<String>): Int {
     return 1
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val simpleText = """
        112999
        911111
    """.trimIndent()
    val part1a = part1(simpleText.lines())
    check(part1a == 7) {
        part1a
    }

    val testInput = readInput("Day18_test")
    val part1 = part1(testInput)
    check(part1 == 102) {
        part1
    }
//    check(part2(testInput) == 51)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 8901)
    check(part2(input) == 9064)
}

