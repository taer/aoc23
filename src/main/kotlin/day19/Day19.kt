package day19

import println
import readInput
import split
import java.lang.RuntimeException


fun main() {
    data class Condition(
        val test: (List<Int>) -> Boolean,
        val destination: String,
        val which: Int,
        val pointInRange: Int,
        val lessThan: Boolean
    )

    data class Conditions(val conds: List<Condition>) {
        fun runFlow(part: List<Int>): String {
            return conds.first { it.test(part) }.destination
        }
    }

    fun conditions(conditions: List<String>) = conditions.map {
        val cond = it.split(":")
        if (cond.size == 1) {
            Condition({ true }, cond.single(), -1, 4001, true)
        } else {
            val thing = cond.first()
            val which = when (thing[0]) {
                'x' -> 0
                'm' -> 1
                'a' -> 2
                's' -> 3
                else -> throw RuntimeException(thing)
            }
            val value = thing.substring(2).toInt()
            when (thing[1]) {
                '<' -> {

                    Condition({ list: List<Int> -> list[which] < value }, cond[1], which, value, true)

                }

                '>' -> {

                    Condition({ list: List<Int> -> list[which] > value }, cond[1], which, value, false)

                }

                else -> throw RuntimeException(thing)
            }
        }
    }

    fun workflowParse(workFlows: List<String>) = workFlows.associate {
        val name = it.substringBefore("{")
        val conditions = it.substringAfter("{").substringBefore("}").split(",")
        name to Conditions(conditions(conditions))
    }

    fun parseParts(parts: List<String>) = parts.map {
        it.trim('{', '}')
            .split(",")
            .map { it.substringAfter("=") }
            .map { it.toInt() }
    }

    fun part1(input: List<String>): Long {
        val split = input.asSequence().split { it.isBlank() }.toList()
        val parts = parseParts(split[1])
        val worksFlows = workflowParse(split[0])

        return parts.sumOf {
            var runFlow = worksFlows.getValue("in").runFlow(it)
            while (runFlow != "A" && runFlow != "R") {
                runFlow = worksFlows.getValue(runFlow).runFlow(it)
            }
            if (runFlow == "A") {
                it.sumOf { it.toLong() }
            } else
                0
        }
    }

    fun chopIntervals(worksFlows: Map<String, Conditions>, ranges: List<IntRange>, lookup: String): Long {
        if (lookup == "A") {
            return ranges.fold(1L) { acc, item -> acc * (item.last - item.first + 1) }
        } else if (lookup == "R") {
            return 0
        }
        val value = worksFlows.getValue(lookup)
        var rangesCopy = ranges
        var result = 0L
        value.conds.forEach {
            if (it.which == -1) {
                result += chopIntervals(worksFlows, rangesCopy, it.destination)
            } else {
                val theRange = rangesCopy[it.which]
                if (it.lessThan) {
                    if (theRange.last < it.pointInRange) {
                        result += chopIntervals(worksFlows, rangesCopy, it.destination)
                    } else if (it.pointInRange < theRange.first) {
                        //nada
                    } else {
                        val passedRange = theRange.first.. it.pointInRange -1
                        val failedRange = it.pointInRange..theRange.last
                        val toMutableList1 = rangesCopy.toMutableList()
                        toMutableList1[it.which] = passedRange
                        result += chopIntervals(worksFlows, toMutableList1, it.destination)

                        val toMutableList2 = rangesCopy.toMutableList()
                        toMutableList2[it.which] = failedRange
                        rangesCopy = toMutableList2
                    }
                } else {
                    if (theRange.first > it.pointInRange) {
                        result += chopIntervals(worksFlows, rangesCopy, it.destination)
                    } else if (it.pointInRange > theRange.last) {
                        //nada
                    } else {
                        val passedRange = it.pointInRange + 1..theRange.last
                        val failedRange = theRange.first..it.pointInRange
                        val toMutableList1 = rangesCopy.toMutableList()
                        toMutableList1[it.which] = passedRange
                        result += chopIntervals(worksFlows, toMutableList1, it.destination)

                        val toMutableList2 = rangesCopy.toMutableList()
                        toMutableList2[it.which] = failedRange
                        rangesCopy = toMutableList2
                    }
                }
            }
        }
        return result


    }

    fun part2(input: List<String>): Long {
        val split = input.asSequence().split { it.isBlank() }.toList()
        val worksFlows = workflowParse(split[0])

        val ranges = listOf(1..4000, 1..4000, 1..4000, 1..4000)
        return chopIntervals(worksFlows, ranges, "in")
    }


    val testInput = readInput("Day19_test")
    val part1 = part1(testInput)
    check(part1 == 19114L) {
        part1
    }
    val part2 = part2(testInput)
    check(part2 == 167409079868000) {
        part2
    }

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 492702L)
    check(part2(input) == 138616621185978)
}

