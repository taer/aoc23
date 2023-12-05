package day05

import kotlinx.coroutines.*
import println
import readInput
import split

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

        val partials = runBlocking {
            withContext(Dispatchers.Default) {
                seeds.chunked(2)
                    .map { it[0] until (it[0] + it[1]) }
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
    part2(input).println()
    check(part1(input) == 331445006L)
    check(part2(input) == 6472060L)
}
