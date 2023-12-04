package day04

import println
import readInput

fun main() {
    fun double(wins: Int): Int {
        if(wins==0) return 0
        var nummer = 1
        repeat(wins-1){
            nummer *=2
        }
        return nummer //use a fold. just dont wanna
    }
    val delimiters = " +".toRegex()
    fun part1(input: List<String>): Int {
        val wins = input.map {
            val (winners, ours) = it.split(":", limit = 2)[1]
                .split("|", limit = 2)
                .map {
                    it.trim()
                        .split(delimiters)
                        .map { it.toInt() }
                }
            ours.count { winners.contains(it) }
        }
        return wins.sumOf {
            double(it)
        }
    }

    fun part2(input: List<String>): Long {
return 1L
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
//    check(part2(testInput) == 467835L)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
//    check(part1(input) == 528799)
//    check(part2(input) == 84907174L)
}
