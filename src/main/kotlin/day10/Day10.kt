package day10

import point.*
import println
import readInput

fun main() {


    fun loopit(
        start: Point,
        xx: Map<Point, Char>,
    ): MutableList<Point> {
        val answer = mutableListOf(start)
        val seen = mutableSetOf(start)
        var depth = 0
        var currentPont = start
        while(true){
            val cardinalsWithDir = currentPont.cardinalsWithDir()
            val xxx = cardinalsWithDir.firstOrNull { (dir, p) ->
                val (possibles, source) = when (dir) {
                    Direction.S -> setOf('|', 'L', 'J') to setOf('|', '7', 'F','S')
                    Direction.N -> setOf('|', '7', 'F') to setOf('|', 'L', 'J','S')
                    Direction.E -> setOf('-', '7', 'J') to setOf('-', 'L','F','S')
                    Direction.W -> setOf('-', 'F', 'L') to setOf('-','7','J','S')
                }
                if(p in xx && !seen.contains(p)){
                    val valueAtNeighbor = xx.getValue(p)
                    val currentData = xx.getValue(currentPont)
                    if (possibles.contains(valueAtNeighbor) && source.contains(currentData)) {
                        depth++
                        seen.add(p)
                        currentPont=p
                        answer.add(p)
                        true
                    } else {
                        false
                    }
                }else{
                    false
                }
            }
            if(xxx == null){
                answer.add(start)
                return answer
            }
        }
    }

    fun part1(input: List<String>): Int {
        val xx = input.flatMapIndexed { x , row ->
            row.mapIndexed { y, data ->
                val p  = point.Point(x,y)
                p to data
            }
        }.toMap()
        val start = requireNotNull( xx.entries.find { it.value=='S' }).key

        val data = loopit(start, xx)
        val i = data.size / 2
        return i
    }

    fun part2(input: List<String>): Int {
        val xx = input.flatMapIndexed { x , row ->
            row.mapIndexed { y, data ->
                val p  = point.Point(x,y)
                p to data
            }
        }.toMap().toMutableMap()
        val start = requireNotNull( xx.entries.find { it.value=='S' }).key

        val path = loopit(start, xx)
//        xx.forEach {
//            val onPath = path.contains(it.key)
//            if(!onPath){
//                xx[it.key]='.'
//            }
//        }

        val maxX = xx.keys.maxBy { it.x }.x
        val maxY = xx.keys.maxBy { it.y }.y
        val points = xx.filter { it.key !in path }.keys
        val potentials = points
            .asSequence()
            .filterNot { it.x == 0 }
            .filterNot { it.y == 0 }
            .filterNot { it.x == maxX }
            .filterNot { it.y == maxY }
            .toList()
        val insides = potentials
            .map {
                var pipescrossed = ""
                var thePoint = it
                while(thePoint.x <= maxX) {
                    if(thePoint in path){
                        val whereAreWe = xx.getValue(thePoint)
                        pipescrossed += whereAreWe
                    }
                    thePoint=thePoint.copy(x=thePoint.x+1)
                }
                pipescrossed
            }
        val regex = "F\\|*J".toRegex()
        val regex2 = "7\\|*L".toRegex()
        val filtered = insides.filter {
            val horizontals = it.count { it == '-' }
            val findAll = regex.findAll(it).count()
            val findAll2 = regex2.findAll(it).count()
            (findAll+findAll2+horizontals) % 2 == 1
        }

        return filtered.size
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
    check(part2(input) == 889)
}

