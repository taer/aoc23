package day13

import makeColumns
import println
import readInput
import split
import java.lang.RuntimeException
import kotlin.math.min

fun main() {


    fun reflect(map: List<List<Char>>, mode: String, data: Pair<String, Long>?): Long {
        for (i in 1 until map.size) {
            val left = map.take(i).reversed()
            val right = map.drop(i)
            val leftSize = left.size
            val rightSize = right.size
            val toCompare = min(leftSize, rightSize)

            val leftChopped = left.take(toCompare)
            val rightChopped = right.take(toCompare)
            if (!(data?.first == mode && data?.second == i.toLong())) {
                if (leftChopped == rightChopped) {
                    return i.toLong()
                }
            }
        }
        return 0
    }

    fun solvePuzzle(puzzle: List<String>, data: Pair<String, Long>? = null): Pair<Long, Pair<String, Long>> {
        val map = puzzle.map { it.toList() }

        val rows = reflect(map, "r", data)
        return if(rows != 0L){
            (100 * rows) to ("r" to rows)
        }else{
            val columns = map.makeColumns()
            val reflect = reflect(columns, "c", data)
            reflect to ("c" to reflect)
        }
    }

    fun part1(input: List<String>): Long {
        val split = input.asSequence().split { it.isBlank() }
        return split.sumOf {
            solvePuzzle(it).first
        }

    }

    fun loopy(it: List<String>): Long {
        val (originalValue, data) = solvePuzzle(it)
        for (x in it.indices) {
            for (y in it.first().indices) {
                val toggle = it[x][y]
                val newValue = if (toggle == '#') '.' else '#'
                val chars = it[x].toCharArray()
                chars[y] = newValue

                val newInput = it.toMutableList()
                newInput[x] = chars.concatToString()
                val potential = solvePuzzle(newInput, data)
                if(potential.first!=0L){
                    return potential.first
                }
            }
        }
        throw RuntimeException()
    }

    fun part2(input: List<String>): Long {
        val split = input.asSequence().split { it.isBlank() }
        return split.sumOf {
            loopy(it)
        }
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405L)
    check(part2(testInput) == 400L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 35538L)
    check(part2(input) == 30442L)
}

