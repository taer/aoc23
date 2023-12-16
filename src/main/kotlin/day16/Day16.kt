package day16

import point.Direction
import point.Point
import point.move
import println
import readInput
import java.util.*

fun main() {

    data class State(val point: Point, val movingTowards: Direction)

    fun State.makeMove(dir: Direction? = null) = (dir ?: this.movingTowards).let {
        copy(
            movingTowards = it,
            point = point.move(it)
        )
    }

    fun List<List<Char>>.inMap(point: Point) =
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
                        Direction.N -> Direction.E
                        Direction.S -> Direction.W
                        Direction.E -> Direction.N
                        Direction.W -> Direction.S
                    }
                    queue.add(currentState.makeMove(newDir))
                }

                '\\' -> {
                    val newDir = when (currentState.movingTowards) {
                        Direction.N -> Direction.W
                        Direction.S -> Direction.E
                        Direction.E -> Direction.S
                        Direction.W -> Direction.N
                    }
                    queue.add(currentState.makeMove(newDir))
                }

                '|' -> {
                    when (currentState.movingTowards) {
                        Direction.N, Direction.S -> queue.add(currentState.makeMove())

                        Direction.E, Direction.W -> {
                            queue.add(currentState.makeMove(Direction.N))
                            queue.add(currentState.makeMove(Direction.S))
                        }
                    }
                }

                '-' -> {
                    when (currentState.movingTowards) {
                        Direction.E, Direction.W -> queue.add(currentState.makeMove())

                        Direction.N, Direction.S -> {
                            queue.add(currentState.makeMove(Direction.E))
                            queue.add(currentState.makeMove(Direction.W))
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
        val start = State(Point(0, 0), Direction.E)
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
                State(Point(0, it), Direction.S),
                State(Point(map.size - 1, it), Direction.N),
            )
        }

        val startEW = map.indices.flatMap {
            listOf(
                State(Point(it, 0), Direction.E),
                State(Point(it, map.first().size - 1), Direction.W),
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

