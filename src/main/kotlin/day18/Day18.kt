package day18

import point.Point2d
import println
import readInput
import java.lang.RuntimeException


fun main() {
    fun shoelace(route: List<Point2d>): Long {
        val area = route.zipWithNext()
            .sumOf { (first, second) ->
                val i = (first.x.toLong() * second.y) - (first.y.toLong() * second.x)
                i
            } / 2
        return area
    }

    fun part1Parse(input: List<String>): List<Pair<String, Int>> {
        val baseParse = input.map {
            val (dir, amounbtStr,_) = it.split(" ", limit = 3)
            dir to amounbtStr.toInt()
        }
        return baseParse
    }
    fun part2Parse(input: List<String>): List<Pair<String, Int>> {
        val baseParse = input.map {
            val poop  = it.substringAfter("(#").substringBefore(")")
            val take = poop.take(5)
            val amount = take.toInt(16)
            val dir = when(poop.last()){
                '0' -> "R"
                '1' -> "D"
                '2' -> "L"
                '3' -> "U"

                else -> throw RuntimeException(poop.last().toString())
            }
            dir to amount
        }
        return baseParse
    }

    fun areaOfLava(baseParse: List<Pair<String, Int>>, origin: Point2d): Long {
        val data = baseParse.map { (dir, amount) ->
            val direction = when (dir) {
                "U" -> Point2d.N
                "D" -> Point2d.S
                "L" -> Point2d.W
                "R" -> Point2d.E
                else -> throw RuntimeException(dir)
            }
            direction to amount
        }
        val route = data.runningFold(origin) { acc, (dir, dist) ->
            acc + (dir * dist)
        }

        val area = shoelace(route)


        val circumf = data.sumOf { it.second }

        return area + circumf / 2 + 1
    }

    fun part1(input: List<String>): Long {
        val baseParse = part1Parse(input)
        val origin = Point2d(0,0)

        return areaOfLava(baseParse, origin)

    }

    fun part2(input: List<String>): Long {
        val baseParse = part2Parse(input)
        val origin = Point2d(0,0)

        return areaOfLava(baseParse, origin)    }

    val example = listOf(
        Point2d(1,6),
        Point2d(3,1),
        Point2d(7,2),
        Point2d(4,4),
        Point2d(8,5),
        Point2d(1,6),
    )
    check(shoelace(example) == 16L){
        shoelace(example)
    }


    val testInput = readInput("Day18_test")
    val part1 = part1(testInput)
    check(part1 == 62L) {
        part1
    }
    check(part2(testInput) == 952408144115)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 36807L)
    check(part2(input) == 9064L)
}

