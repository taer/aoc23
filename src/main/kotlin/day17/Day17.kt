package day17

import point.Direction
import point.Point
import point.cardinalsWithDir
import point.dirTo
import println
import readInput

private fun Point.inMap(map: List<List<Int>>): Boolean {
    //x is row here
    return x >= 0 && y>=0 && x< map.size && y<map.first().size
}

fun main() {


    data class Thingie(
        val distance: Int,
        val direction: Direction?,
        val len: Int
    )
    fun pathFind(map: List<List<Int>>, start: Point, end: Point): Pair<MutableMap<Point, Point>, MutableMap<Point, Int>> {
        val distances = mutableMapOf<Point, Int>().withDefault { Int.MAX_VALUE }
        val distances2 = mutableMapOf<Point, Thingie>()
        val previous = mutableMapOf<Point, Point>()
        val visited = mutableSetOf<Point>()
        distances[start]=0
        distances2[start]=Thingie(0,null,0)
        val queue = mutableSetOf(start)
        while(queue.isNotEmpty()){
            val current = queue.minBy { distances.getValue(it) }
            queue.remove(current)
            val distanceSoFar = distances.getValue(current)

            fun getTravelToPointDistance(current: Point, potentialNext: Point, dir: Direction): Int? {

                val getHere = mutableListOf(current)
                var point: Point? = current
                while(point!=null && getHere.size<4){
                    point =previous[point]
                    point?.let {
                        getHere.add(it)
                    }
                }
                val dires = getHere.windowed(2){
                    it[0].dirTo(it[1])
                }
                if(dires.size==3){
                    if( dires.all { it == dir })
                        return null
                }


                return map[potentialNext.x][potentialNext.y]
            }

            current.cardinalsWithDir()
                .asSequence()
                .filter { it.second.inMap(map) }
                .filter { it.second !in visited }
                .map { it to  distances.getValue(it.second)}
                .forEach { (aa, pointsDistance) ->
                    val ( dir, potentialNext) = aa
                    val travelToPointDistance = getTravelToPointDistance(current, potentialNext, dir)
                    if(travelToPointDistance!=null){
                        if(distanceSoFar + travelToPointDistance < pointsDistance){
                            distances[potentialNext] = distanceSoFar + travelToPointDistance
                            previous[potentialNext] = current
                        }
                        queue.add(potentialNext)
                    }
                }
            visited.add(current)

        }

        return previous to distances


    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.map { it.digitToInt() } }
        val end = Point(map.size - 1, map.first().size - 1)
        val (path, distances) = pathFind(map, Point(0,0), end)
        val i = distances[Point(map.size - 1, map.first().size - 1)]
        val mapCopy = input.map { it.map { it }.toMutableList() }.toMutableList()
        var point = path[end]
        while(point!=null){
            mapCopy[point!!.x][point!!.y] = '*'
            point =path[point]
        }
        mapCopy.forEach {
            println(it.joinToString(""))
        }
        return i!!
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day17_test")
    val testtt = """
        112999
        911111
    """.trimIndent()
    val part1a = part1(testtt.lines())
    check(part1a == 7) {
        part1a
    }
    val part1 = part1(testInput)
    check(part1 == 102) {
        part1
    }
//    check(part2(testInput) == 51)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 8901)
    check(part2(input) == 9064)
}

