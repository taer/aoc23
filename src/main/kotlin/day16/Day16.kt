package day16

import point.DeprecatedDirection
import point.DeprecatedPoint
import point.move
import println
import readInput
import java.util.*

fun main() {

    data class State(val point: DeprecatedPoint, val movingTowards: DeprecatedDirection)

    fun State.makeMove(dir: DeprecatedDirection? = null) = (dir ?: this.movingTowards).let {
        copy(
            movingTowards = it,
            point = point.move(it)
        )
    }

    fun List<List<Char>>.inMap(point: DeprecatedPoint) =
        (point.x >= 0 && point.y >= 0 && point.x < size && point.y < first().size)


    fun doMoves(start: State, map: List<List<Char>>): Array<Array<Int>> {
        val seenNodes = mutableSetOf<State>()
        val colors = Array(map.size) {
            Array(map.first().size) { 0 }
        }
        val queue = LinkedList<State>().also { it.add(start) }

        while (queue.isNotEmpty()) {
            val currentState = queue.pop()

            if (!map.inMap(currentState.point)) {
                continue
            }

            if (seenNodes.contains(currentState)) {
                continue
            }
            seenNodes.add(currentState)
            colors[currentState.point.x][currentState.point.y]++

            when (map[currentState.point.x][currentState.point.y]) {
                '.' -> {
                    queue.add(currentState.makeMove())
                }

                '/' -> {
                    val newDir = when (currentState.movingTowards) {
                        DeprecatedDirection.N -> DeprecatedDirection.E
                        DeprecatedDirection.S -> DeprecatedDirection.W
                        DeprecatedDirection.E -> DeprecatedDirection.N
                        DeprecatedDirection.W -> DeprecatedDirection.S
                    }
                    queue.add(currentState.makeMove(newDir))
                }

                '\\' -> {
                    val newDir = when (currentState.movingTowards) {
                        DeprecatedDirection.N -> DeprecatedDirection.W
                        DeprecatedDirection.S -> DeprecatedDirection.E
                        DeprecatedDirection.E -> DeprecatedDirection.S
                        DeprecatedDirection.W -> DeprecatedDirection.N
                    }
                    queue.add(currentState.makeMove(newDir))
                }

                '|' -> {
                    when (currentState.movingTowards) {
                        DeprecatedDirection.N, DeprecatedDirection.S -> queue.add(currentState.makeMove())

                        DeprecatedDirection.E, DeprecatedDirection.W -> {
                            queue.add(currentState.makeMove(DeprecatedDirection.N))
                            queue.add(currentState.makeMove(DeprecatedDirection.S))
                        }
                    }
                }

                '-' -> {
                    when (currentState.movingTowards) {
                        DeprecatedDirection.E, DeprecatedDirection.W -> queue.add(currentState.makeMove())

                        DeprecatedDirection.N, DeprecatedDirection.S -> {
                            queue.add(currentState.makeMove(DeprecatedDirection.E))
                            queue.add(currentState.makeMove(DeprecatedDirection.W))
                        }
                    }
                }

                else -> throw RuntimeException("foo ")
            }
        }
        return colors
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toList() }
        val start = State(DeprecatedPoint(0, 0), DeprecatedDirection.E)
        val colors = doMoves(start, map)
//        println()
//        state.colors.forEach {
//            println(it.joinToString("") { if (it == 0) "." else "#" })
//        }
//        println()
        return colors.sumOf { pp -> pp.count { aa -> aa != 0 } }
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toList() }
        val startNS = (map.first().indices).flatMap {
            listOf(
                State(DeprecatedPoint(0, it), DeprecatedDirection.S),
                State(DeprecatedPoint(map.size - 1, it), DeprecatedDirection.N),
            )
        }

        val startEW = map.indices.flatMap {
            listOf(
                State(DeprecatedPoint(it, 0), DeprecatedDirection.E),
                State(DeprecatedPoint(it, map.first().size - 1), DeprecatedDirection.W),
            )
        }
        val total = startEW + startNS

        return total.maxOf {
            val colors = doMoves(it, map)
            colors.sumOf { pp -> pp.count { aa -> aa != 0 } }
        }
    }

    val testInput = readInput("Day16_test")
    val part1 = part1(testInput)
    check(part1 == 46) {
        part1
    }
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 8901)
    check(part2(input) == 9064)
}

