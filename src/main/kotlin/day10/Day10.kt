package day10

import point.*
import println
import readInput
import java.lang.RuntimeException
import kotlin.time.measureTimedValue

fun main() {
    fun findPath(
        startingMap: Map<DeprecatedPoint, Char>,
    ): Pair<MutableList<DeprecatedPoint>, Map<DeprecatedPoint, Char>> {
        val start = requireNotNull(startingMap.entries.find { it.value == 'S' }).key

        val path = mutableListOf(start)
        val seen = mutableSetOf(start)
        var depth = 0
        var currentPont = start
        var startDirection: DeprecatedDirection? = null
        var endDirection: DeprecatedDirection? = null
        while(true){
            val cardinalsWithDir = currentPont.cardinalsWithDir()
            val xxx = cardinalsWithDir.firstOrNull { (destinationDir, p) ->
                val (possibles, source) = when (destinationDir) {
                    DeprecatedDirection.S -> setOf('|', 'L', 'J') to setOf('|', '7', 'F','S')
                    DeprecatedDirection.N -> setOf('|', '7', 'F') to setOf('|', 'L', 'J','S')
                    DeprecatedDirection.E -> setOf('-', '7', 'J') to setOf('-', 'L','F','S')
                    DeprecatedDirection.W -> setOf('-', 'F', 'L') to setOf('-','7','J','S')
                }
                if(startDirection!=null && p == start){
                    endDirection = destinationDir.reversed()
                }
                if(p in startingMap && !seen.contains(p)){
                    val valueAtNeighbor = startingMap.getValue(p)
                    val currentData = startingMap.getValue(currentPont)
                    if (possibles.contains(valueAtNeighbor) && source.contains(currentData)) {
                        if(currentPont==start && startDirection==null){
                            startDirection = destinationDir
                        }
                        depth++
                        seen.add(p)
                        currentPont=p
                        path.add(p)
                        true
                    } else {
                        false
                    }
                }else{
                    false
                }
            }
            if(xxx == null){
                path.add(start)
                val startShouldBe = when(val foo = setOf(startDirection, endDirection)){
                    setOf(DeprecatedDirection.N, DeprecatedDirection.S) -> '|'
                    setOf(DeprecatedDirection.E, DeprecatedDirection.W) -> '-'
                    setOf(DeprecatedDirection.N, DeprecatedDirection.E) -> 'L'
                    setOf(DeprecatedDirection.N, DeprecatedDirection.W) -> 'J'
                    setOf(DeprecatedDirection.S, DeprecatedDirection.W) -> '7'
                    setOf(DeprecatedDirection.S, DeprecatedDirection.E) -> 'F'
                    else -> throw RuntimeException(foo.toString())
                }
                val toMutableMap = startingMap.toMutableMap()
                toMutableMap[start] = startShouldBe
                return path to toMutableMap
            }
        }
    }

    fun parseInput(input: List<String>): Pair<List<DeprecatedPoint>, Map<DeprecatedPoint, Char>> {
        val theMap = input.flatMapIndexed { x, row ->
            row.mapIndexed { y, data ->
                val p = DeprecatedPoint(x, y)
                p to data
            }
        }.toMap()
        return findPath(theMap)
    }

    fun raytrace(
        theMap: Map<DeprecatedPoint, Char>,
        path: List<DeprecatedPoint>
    ): Int {
        val maxX = theMap.keys.maxBy { it.x }.x
        val maxY = theMap.keys.maxBy { it.y }.y
        val crossedFJ = "F\\|*J".toRegex()
        val crossed7L = "7\\|*L".toRegex()

        return theMap.keys.asSequence()
            .filter { it !in path }
            .filterNot { it.x == 0 }
            .filterNot { it.y == 0 }
            .filterNot { it.x == maxX }
            .filterNot { it.y == maxY }
            .map { scanFrom ->
                val pipescrossed = mutableListOf<Char>()
                var thePoint = scanFrom
                while (thePoint.x <= maxX) {
                    if (thePoint in path) {
                        val whereAreWe = theMap.getValue(thePoint)
                        pipescrossed.add(whereAreWe)
                    }
                    thePoint = thePoint.copy(x = thePoint.x + 1)
                }
                pipescrossed.joinToString("")
            }.filter {
                val horizontals = it.count { data -> data == '-' }
                val crossingsFJ = crossedFJ.findAll(it).count()
                val crossing7L = crossed7L.findAll(it).count()
                (crossingsFJ + crossing7L + horizontals) % 2 == 1
            }.count()

    }
    fun part1(input: List<String>): Int {
        val (path, _) = parseInput(input)
        return path.size / 2
    }

    fun part2(input: List<String>): Int {
        val (path, theMap) = parseInput(input)

        val (filtered, duration) = measureTimedValue { raytrace(theMap, path) }
        println("Raytrace took $duration")
        return filtered
    }


    val testInput = readInput("Day10_test")
    val testInput2 = readInput("Day10_test2")
    val part1 = part1(testInput)
    check(part1 == 4)
    val part1a = part1(testInput2)
    check(part1a == 8)

    val insides = """
        ...........
        .S-------7.
        .|F-----7|.
        .||.....||.
        .||.....||.
        .|L-7.F-J|.
        .|..|.|..|.
        .L--J.L--J.
        ...........
    """.trimIndent().lines()
    check(part2(insides) == 4)
    val insides2 = """
        .F----7F7F7F7F-7....
        .|F--7||||||||FJ....
        .||.FJ||||||||L7....
        FJL7L7LJLJ||LJ.L-7..
        L--J.L7...LJS7F-7L7.
        ....F-J..F7FJ|L7L7L7
        ....L7.F7||L7|.L7L7|
        .....|FJLJ|FJ|F7|.LJ
        ....FJL-7.||.||||...
        ....L---J.LJ.LJLJ...
    """.trimIndent().lines()
    val part2 = part2(insides2)
    check(part2 == 8)
    val insides3 = """
        FF7FSF7F7F7F7F7F---7
        L|LJ||||||||||||F--J
        FL-7LJLJ||||||LJL-77
        F--JF--7||LJLJ7F7FJ-
        L---JF-JLJ.||-FJLJJ7
        |F|F-JF---7F7-L7L|7|
        |FFJF7L7F-JF7|JL---7
        7-L-JL7||F7|L7F-7F7|
        L.L7LFJ|||||FJL7||LJ
        L7JLJL-JLJLJL--JLJ.L
    """.trimIndent().lines()
    val part2a = part2(insides3)
    check(part2a == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 6864)
    check(part2(input) == 349)
}

