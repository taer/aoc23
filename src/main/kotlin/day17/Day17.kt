package day17

import point.Point2d
import println
import readInput
import java.lang.RuntimeException
import java.util.PriorityQueue


fun main() {
    data class State(val location:Point2d, val directon: Point2d, val distanceSoFar:Int){
        fun moveTowards(dir:Point2d): State {
            return State(
                location = location+dir,
                directon = dir,
                distanceSoFar = if(directon == dir) distanceSoFar+1 else 1
                )
        }
    }
    data class Work(val state: State, val heatloss:Int)
    fun List<List<Int>>.isInside(point: Point2d): Boolean {
        val b = point.y in indices && point.x in first().indices
        return b
    }

    fun moveCart(heats: List<List<Int>>, start: Point2d, end: Point2d): Int {
        val seen  = mutableSetOf<State>()
        val work = PriorityQueue<Work>(compareBy { it.heatloss })

        while(work.isNotEmpty()){
            val (current, heatloss) = work.poll()
            if(current.location == end) return heatloss



        }

        throw RuntimeException("crap")
    }

    fun part1(input: List<String>): Int {
        val heats = input.map { it.map { it.digitToInt() } }
        val start = Point2d(0,0)
        val end = Point2d(heats.first().lastIndex, heats.lastIndex)
        return moveCart(heats, start, end)
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

