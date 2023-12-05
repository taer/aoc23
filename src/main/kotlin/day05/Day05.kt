package day05

import kotlinx.coroutines.*
import println
import readInput
import split
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis
import kotlin.time.measureTimedValue

fun main() {

    class MapRow(rowData: List<Long>){
        val to = rowData[0] until (rowData[0] + rowData[2])
        val from = rowData[1] until (rowData[1] + rowData[2])
        fun contains(source: Long) = from.contains(source)
        fun mapFrom(source: Long): Long {
            val distance = source - from.first
            return to.first + distance
        }
    }

    class TheMap(val rows: List<MapRow>){
        fun mapInput(source: Long) = rows.find { it.contains(source) }?.mapFrom(source) ?: source
    }

    fun parse(input:List<String>): Pair<List<Long>, List<TheMap>> {
        val seeds = input.first().substringAfter(":").split(" ").filterNot { it.isEmpty() }.map { it.toLong() }
        val maps = input.asSequence().drop(2).split { it.isBlank() }

        val theMaps = maps.map {
            val aMap = it.drop(1)
                .map {
                    val map = it.split(" ").map { it.toLong() }
                    MapRow(map)
                }
            TheMap(aMap)
        }
        return seeds to theMaps.toList()
    }

    fun mapWalker(seeds: Iterable<Long>, theMaps: List<TheMap>) = seeds.minOf { seedNum ->
        theMaps.fold(seedNum) { nextLookup: Long, theMap: TheMap ->
            theMap.mapInput(nextLookup)
        }
    }

    fun part1(input: List<String>): Long {
        val (seeds, theMaps) = parse(input)
        return mapWalker(seeds, theMaps)
    }

    fun part2(input: List<String>): Long {
        val (seeds, theMaps) = parse(input)

        val subdivided = seeds.chunked(2)
            .map { it[0] until (it[0] + it[1]) }.flatMap { range ->
                val maxSize = 100_000_000
                if (range.count() > maxSize) {
                    val braakTo = range.count()  / maxSize
                    val extra = if ((range.count()  % maxSize ) == 0) 0 else 1
                    List(braakTo+extra) {
                        val start = range.first + (it * maxSize)
                        val end = if ((start + maxSize) < range.last + 1) start + maxSize else range.last + 1
                        start until end
                    }
                } else {
                    listOf(range)
                }
            }
        val normal = seeds.chunked(2)
            .map { it[0] until (it[0] + it[1]) }


        val partials = runBlocking {
            withContext(Dispatchers.Default) {
              subdivided
                    .map {
                        async { mapWalker(it, theMaps) }
                    }.awaitAll()
            }
        }

        return partials.min()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    val (part2Result, time) = measureTimedValue { part2(input) }
    println("Took ${time.inWholeMilliseconds}")
    println("Took $time")
    part2Result.println()
    check(part1(input) == 331445006L)
    check(part2Result == 6472060L)
}
