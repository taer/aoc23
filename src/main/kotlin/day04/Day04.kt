package day04

import println
import readInput
import kotlin.math.pow

fun main() {
    fun double(wins: Int) = 2.0.pow(wins-1).toInt()

    val delimiters = " +".toRegex()

    fun winsFromInput(input: List<String>) = input.map {
        val (winners, ours) = it.split(":", limit = 2)[1]
            .split("|", limit = 2)
            .map {
                it.trim()
                    .split(delimiters)
                    .map { it.toInt() }
            }
        ours.count { ourNumber -> winners.contains(ourNumber) }
    }

    fun part1(input: List<String>): Int {
        val wins = winsFromInput(input)
        return wins.sumOf {
            double(it)
        }
    }

    fun part2(input: List<String>): Int {
        val wins = winsFromInput(input)

        val cardCount = MutableList(input.size){ 1 }
        wins.forEachIndexed{index, winsForCard ->
            repeat(cardCount[index]) {
                repeat(winsForCard) { cardToAddTo ->
                    val toMod = index + cardToAddTo + 1
                    if (toMod < wins.size) {
                        cardCount[toMod] += 1
                    }
                }
            }
        }
        return cardCount.sum()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 25174)
    check(part2(input) == 6420979)
}
