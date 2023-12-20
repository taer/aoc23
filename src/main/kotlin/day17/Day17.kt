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

    fun moveCart(heats: List<List<Int>>, start: Point2d, end: Point2d, minMove: Int = 1, poo: (State, Point2d) -> Boolean): Int {
        val seen  = mutableSetOf<State>()
        val work = PriorityQueue<Work>(compareBy { it.heatloss })
        val startState = State(start, Point2d.E, 0)
        work.add(Work(startState, 0))
        seen.add(startState)
        val ways =  mapOf(
            Point2d.N to Point2d.cardinals-Point2d.S,
            Point2d.S to Point2d.cardinals-Point2d.N,
            Point2d.E to Point2d.cardinals-Point2d.W,
            Point2d.W to Point2d.cardinals-Point2d.E,
            )

        while(work.isNotEmpty()){
            val (current, heatloss) = work.poll()
            if(current.location == end && current.distanceSoFar>=minMove)
                return heatloss

            val value = ways.getValue(current.directon)
            val map = value
            val filter = map
                .filter {
                    val inside = heats.isInside(current.location+ it)
                    inside
                }
            filter
                .filter {
                    val poo1 = poo(current, it)
                    poo1
                }
                .map { current.moveTowards(it) }
                .filter {
                    val b = it !in seen
                    b
                }
                .forEach {
                    val element = Work(it, heatloss + heats[it.location.y][it.location.x])
                    seen.add(it)
                    work.add(element)

                }




        }

        throw RuntimeException("crap")
    }

    fun part1(input: List<String>): Int {
        val heats = input.map { it.map { it.digitToInt() } }
        val start = Point2d(0,0)
        val end = Point2d(heats.first().lastIndex, heats.lastIndex)
        return moveCart(heats, start, end) { it,newDir -> it.distanceSoFar < 3 ||it.directon!=newDir}
    }

    fun part2(input: List<String>): Int {
        val heats = input.map { it.map { it.digitToInt() } }
        val start = Point2d(0,0)
        val end = Point2d(heats.first().lastIndex, heats.lastIndex)
        return moveCart(heats, start, end, 4) { it,newDir ->
            if(it.distanceSoFar < 4){
                newDir == it.directon
            }else if(it.distanceSoFar>9){
                newDir != it.directon
            }else
                true
        }
    }

    val testInput = readInput("Day17_test")
    val part1 = part1(testInput)
    check(part1 == 102) {
        part1
    }
    check(part2(testInput) == 94)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 1138)
    check(part2(input) == 9064)
}

