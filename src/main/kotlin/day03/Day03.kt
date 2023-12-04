package day03

import point.Point
import point.neighbors
import println
import product
import readInput

fun main() {
    class Accumulator {
        private val numbers = mutableListOf<Char>()
        private val surrounds = mutableSetOf<Point>()
        fun isEmpty() = numbers.isEmpty()
        fun getNeighbors() = surrounds.toSet()
        fun addDigit(c: Char, p: Point) {
            require(c.isDigit()) {
                "$c is not a digit"
            }
            numbers.add(c)
            surrounds.addAll(p.neighbors())
        }

        fun number() = numbers.joinToString("").toInt()
    }

    fun parse(input: List<String>): Pair<List<Accumulator>, Map<Point, Char>> {
        val pointz = mutableMapOf<Point, Char>()
        var accumulator = Accumulator()
        val accumulatorList = mutableListOf<Accumulator>()
        input.forEachIndexed { x, row ->
            row.forEachIndexed { y, char ->
                val p = Point(x, y)
                pointz[p] = char

                if (char.isDigit()) {
                    accumulator.addDigit(char, p)
                } else {
                    if (!accumulator.isEmpty()) {
                        accumulatorList.add(accumulator)
                        accumulator = Accumulator()
                    }
                }
            }
            if (!accumulator.isEmpty()) {
                accumulatorList.add(accumulator)
                accumulator = Accumulator()
            }
        }
        return accumulatorList.toList() to pointz.toMap()
    }

    fun List<Accumulator>.asNumber() = map(Accumulator::number)


    fun part1(input: List<String>): Int {
        fun Char.isSymbol() = !isDigit() && this != '.'

        val (accumulators, grid) = parse(input)
        val accumulatorsWithSymbols = accumulators.filter { accum ->
            accum.getNeighbors().filter {
                it in grid
            }.any { neighbor ->
                grid.getValue(neighbor).isSymbol()
            }
        }

        return accumulatorsWithSymbols.asNumber().sum()
    }

    fun part2(input: List<String>): Long {
        val (accumulators, grid) = parse(input)

        fun Char.isGear() = this == '*'
        fun List<Accumulator>.nextToPoint(p: Point) = filter { it.getNeighbors().contains(p) }

        val gearRatios = grid
            .filter { it.value.isGear() }
            .keys
            .map { gearPoint -> accumulators.nextToPoint(gearPoint) }
            .filter { it.size == 2 }
            .map { accumulator -> accumulator.asNumber() }
            .map { it.product() }
        return gearRatios.sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 528799)
    check(part2(input) == 84907174L)
}
