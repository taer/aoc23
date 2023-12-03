package day02

import println
import product
import readInput
import java.lang.RuntimeException

enum class Day02Color{
    RED, GREEN, BLUE
}
fun main() {

    data class Pull(val color: Day02Color, val num:Int)
    data class Round(val pulls: List<Pull>)
    data class Game(val gameNum: Int, val rounds: List<Round>)
    fun parseGame(input: List<String>): List<Game> {
        return input.map {
            val (gameNum,rest) = it.split(":", limit = 2)
            val pulls = rest.split(";")
            val pp = pulls.map {
                val colors = it.split(",")
                val round = colors.map {
                    val (num, color)  = it.trim().split(" ", limit = 2)
                    if(color=="blue"){
                        Pull(Day02Color.BLUE, num.toInt())
                    }else if(color=="green"){
                        Pull(Day02Color.GREEN, num.toInt())
                    }else if(color=="red"){
                        Pull(Day02Color.RED, num.toInt())
                    }else{
                        throw RuntimeException("No $color")
                    }
                }
                Round(round)
            }
            Game(gameNum.split(" ", limit = 2)[1].toInt(), pp)
        }
    }


    fun canPull(game: Game, sampleTarget: Map<Day02Color, Int>): Boolean {
        return game.rounds.all { round ->
            val combinedPulls = round.pulls.associateBy { it.color }.mapValues { it.value.num }
            combinedPulls.all { entry ->
                val amountOfColor = sampleTarget.getValue(entry.key)
                entry.value <= amountOfColor
            }

        }
    }

    fun part1(input: List<String>, sampleTarget: Map<Day02Color, Int>): Int {
        val parseGame = parseGame(input)
       return parseGame.filter { canPull(it, sampleTarget) }.sumOf { it.gameNum }
    }
    fun part2(input: List<String>): Long {
        val parseGame = parseGame(input)
        return parseGame.sumOf {
            val pulls = it.rounds.map {
                it.pulls.associateBy { it.color }.mapValues { it.value.num }
            }
            val total = mutableMapOf<Day02Color, Int>(
                Day02Color.RED to 0,
                Day02Color.GREEN to 0,
                Day02Color.BLUE to 0,
            )
            pulls.forEach { draw ->
                val red = draw.getOrDefault(Day02Color.RED,0)
                val green = draw.getOrDefault(Day02Color.GREEN,0)
                val blue = draw.getOrDefault(Day02Color.BLUE,0)
                total[Day02Color.RED] = total.getValue(Day02Color.RED).coerceAtLeast(red)
                total[Day02Color.GREEN] = total.getValue(Day02Color.GREEN).coerceAtLeast(green)
                total[Day02Color.BLUE] = total.getValue(Day02Color.BLUE).coerceAtLeast(blue)
            }
            total.values.product()
        }

    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val sampleTarget = mapOf(
        Day02Color.RED to 12,
        Day02Color.GREEN to 13,
        Day02Color.BLUE to 14,
    )
    check(part1(testInput, sampleTarget) == 8)
    check(part2(testInput) == 2286L)

    val input = readInput("Day02")
    check(part1(input, sampleTarget) == 2720)
    part1(input, sampleTarget).println()
    part2(input).println()
    check(part2(input) == 71535L)
}

