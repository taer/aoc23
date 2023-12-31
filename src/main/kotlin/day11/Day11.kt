package day11

import makeColumns
import point.DeprecatedPoint
import println
import readInput
import kotlin.math.absoluteValue

fun main() {
    fun makePairs(arr: List<DeprecatedPoint>): Sequence<Pair<DeprecatedPoint, DeprecatedPoint>> = sequence {
        for(i in 0 until arr.size-1) {
            for(j in i+1 until arr.size) {
                yield(arr[i] to arr[j])
            }
        }
    }

    fun distanceFrom(from: DeprecatedPoint, to: DeprecatedPoint) =
        (from.x - to.x).absoluteValue.toLong() + (from.y - to.y).absoluteValue.toLong()


    fun holeyStars(input: List<String>, multiplier: Int): Long {
        val rowsShift = input.map {
            if (it.any { it == '#' }) 0 else 1
        }.runningReduce { acc, i -> acc + i }

        val map = input.map { it.toList() }.makeColumns()
        val colShift = map.map {
            if (it.any { it == '#' }) 0 else 1
        }.runningReduce { acc, i -> acc + i }

        val points = input.flatMapIndexed { x, row ->
            row.mapIndexedNotNull { y, c ->
                if (c == '#') {
                    DeprecatedPoint(
                        x = x + rowsShift[x] * (multiplier - 1),
                        y = y + colShift[y] * (multiplier - 1)
                    )
                } else {
                    null
                }
            }
        }
        return makePairs(points).sumOf { (from, to) ->
            distanceFrom(from, to)
        }
    }

    fun part1(input: List<String>): Long {
        return holeyStars(input, 2)
    }

    fun part2(input: List<String>): Long {
        return holeyStars(input, 1_000_000)
    }

    val testInput = readInput("Day11_test")
    part1(testInput).println()
    check(part1(testInput) == 374L)
    check(holeyStars(testInput,10) == 1030L)
    check(holeyStars(testInput,100) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 9522407L)
    check(part2(input) == 544723432977L)
}

