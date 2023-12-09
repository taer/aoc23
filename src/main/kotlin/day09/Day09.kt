package day09

import println
import readInput

fun main() {


    fun doTheDeltas(data: List<Int>): List<MutableList<Int>> {
        val deltas = mutableListOf<MutableList<Int>>()
        deltas.add(data.toMutableList())
        val first = data.windowed(2) {
            it[1] - it[0]
        }.toMutableList()
        deltas.add(first)
        while (deltas.last.any { it != 0 }) {
            val xx = deltas.last.windowed(2) {
                it[1] - it[0]
            }.toMutableList()
            deltas.add(xx)
        }
        return deltas
    }

    fun puzzle(data: List<Int>): Int {
        val deltas = doTheDeltas(data)
        var toAddNext = 0
        deltas.reversed().forEach { delta ->
            delta.add(delta.last + toAddNext)
            toAddNext = delta.last
        }
        return deltas.first.last
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val split = it.split(" ").map { it.toInt() }
            puzzle(split)
        }
    }

    fun part2(input: List<String>) = 1


    val testInput = readInput("Day09_test")
    val part1 = part1(testInput)
    part1.println()
    check(part1 == 114)
//    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 1581679977)
    check(part2(input) == 1)
}
