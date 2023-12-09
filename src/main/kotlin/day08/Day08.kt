package day08

import println
import readInput

fun main() {


    fun  String.repeatAsSeq() = sequence { while (true) yieldAll(this@repeatAsSeq.iterator()) }

    fun parse(input: List<String>): Pair<String, Map<String, Pair<String, String>>> {
        val directions = input.first
        val data = input.drop(2).associate {
            val (from, to) = it.split(" = ", limit = 2)
            val (left, right) = to.removeSurrounding("(", ")").split(", ")
            from to (left to right)
        }
        return directions to  data
    }

    fun findPath(dirs: String, map: Map<String, Pair<String, String>>, start:String, complete: (String)->Boolean): Int {
        var count = 0
        var current = start
        val directions = dirs.repeatAsSeq().iterator()
        while (true) {
            val direction = directions.next()
            val nextSlot = map.getValue(current)
            current = when (direction) {
                'L' -> nextSlot.first
                'R' -> nextSlot.second
                else -> throw RuntimeException("BAD $direction")
            }
            count++
            if (complete(current)) {
                return count
            }
        }
    }


    fun gcd(a: Long, b: Long): Long {
        if(b == 0L){
            return a
        }
        return gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        val num = a*b
        val den = gcd(a, b)
        return num/den
    }

    fun lcm(res: List<Long>): Long {
        return res.reduce{lcm, next ->  lcm(lcm, next) }
    }

    fun part1(input: List<String>): Int {
        val (dirs, map) = parse(input)

        return findPath(dirs, map,"AAA") {
            it == "ZZZ"
        }
    }

    fun part2(input: List<String>): Long {
        val (dirs, map) = parse(input)

        val res = map.keys.filter { it.endsWith("A") }.map {
            findPath(dirs,map,it) { current ->
                current.endsWith("Z")
            }
        }.map { it.toLong() }
        return lcm(res)
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    val testInput2 = """
        LLR

        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent().lines()

    check(part1(testInput) == 2)
    check(part1(testInput2) == 6)
    val part2Test = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent().lines()
    check(part2(part2Test) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 21389)
    check(part2(input) == 21083806112641)
}
