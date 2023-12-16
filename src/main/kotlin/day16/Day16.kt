package day16

import point.Direction
import point.Point
import point.move
import println
import readInput
import java.lang.RuntimeException

fun main() {

    data class MovingState(val point: Point, val movingTowards: Direction)

    fun MovingState.moveNext() = point.move(movingTowards)

    fun List<List<Char>>.inMap(point: Point) =
        (point.x >= 0 && point.y >= 0 && point.x < size && point.y < first().size)

    class WalingContext(val map: List<List<Char>>){
        val colors = Array(map.size) {
            Array(map.first().size) { 0 }
        }
        val seenNodes = mutableSetOf<MovingState>()
    }

    fun doMoves(movement: MovingState, state: WalingContext) {
        






        if (!state.map.inMap(movement.point)) {
            return
        }

        if(state.seenNodes.contains(movement)){
            return
        }
        state.seenNodes.add(movement)
        state.colors[movement.point.x][movement.point.y]++

        when (state.map[movement.point.x][movement.point.y]) {
            '.' -> {
                doMoves(movement.copy(point = movement.moveNext()), state)
            }

            '/' -> {
                val newDir = when (movement.movingTowards) {
                    Direction.N -> Direction.E
                    Direction.S -> Direction.W
                    Direction.E -> Direction.N
                    Direction.W -> Direction.S
                }
                doMoves(movement.copy(point = movement.point.move(newDir), movingTowards = newDir), state)
            }

            '\\' -> {
                val newDir = when (movement.movingTowards) {
                    Direction.N -> Direction.W
                    Direction.S -> Direction.E
                    Direction.E -> Direction.S
                    Direction.W -> Direction.N
                }
                doMoves(movement.copy(point = movement.point.move(newDir), movingTowards = newDir), state)
            }

            '|' -> {
                when (movement.movingTowards) {
                    Direction.N, Direction.S ->
                        doMoves(movement.copy(point = movement.moveNext()), state)

                    Direction.E, Direction.W -> {
                        doMoves(movement.copy(point = movement.point.move(Direction.N), movingTowards = Direction.N), state)
                        doMoves(movement.copy(point = movement.point.move(Direction.S), movingTowards = Direction.S), state)
                    }
                }
            }

            '-' -> {
                when (movement.movingTowards) {
                    Direction.E, Direction.W ->
                        doMoves(movement.copy(point = movement.moveNext()), state)

                    Direction.N, Direction.S -> {
                        doMoves(movement.copy(point = movement.point.move(Direction.E), movingTowards = Direction.E), state)
                        doMoves(movement.copy(point = movement.point.move(Direction.W), movingTowards = Direction.W), state)
                    }
                }
            }

            else -> throw RuntimeException("foo ")
        }
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toList() }
        val start = MovingState(Point(0, 0), Direction.E)
        val state = WalingContext(map)
        doMoves(start, state)
        println()
        state.colors.forEach {
            println( it.joinToString("" ){  if(it==0) "." else "#"}   )
        }
        println()
        return state.colors.sumOf { pp -> pp.count { aa -> aa != 0 } }
    }

    fun part2(input: List<String>): Int {
        return 0

    }

    val testInput = readInput("Day16_test")
    val part1 = part1(testInput)
    check(part1 == 46) {
        part1
    }
//    check(part2(testInput) == 145)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
//    check(part1(input) == 494980)
//    check(part2(input) == 247933)
}

