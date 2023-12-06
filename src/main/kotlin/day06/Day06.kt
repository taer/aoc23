package day06

import println
import product
import readInput
import splitWhiteToLong

fun main() {

    fun parseIt(input: List<String>): List<Pair<Long, Long>> {
        val racetimes = input[0].substringAfter(":").splitWhiteToLong()
        val distance = input[1].substringAfter(":").splitWhiteToLong()
        return racetimes.zip(distance)
    }
    fun parseIt2(input: List<String>): List<Pair<Long, Long>> {
        val racetimes = input[0].substringAfter(":").split(" ").filter { it.isNotEmpty() }.joinToString("").toLong()
        val distance = input[1].substringAfter(":").split(" ").filter { it.isNotEmpty() }.joinToString("").toLong()
        return listOf(racetimes to distance)
    }

    fun runRaces(parseIt: List<Pair<Long, Long>>) = parseIt.map { (time, distance) ->
        (1..time).map { windup ->
            val timeLeft = time - windup
            timeLeft * windup
        }.count { it > distance }
    }.product()

    fun part1(input: List<String>) = runRaces(parseIt(input))
    fun part2(input: List<String>) = runRaces(parseIt2(input))

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 1155175L)
    check(part2(input) == 35961505L)
}
