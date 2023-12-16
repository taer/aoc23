package day15

import println
import readInput
import sumOfIndexed
import java.lang.RuntimeException

fun main() {

    fun String. hashIt(): Int{
        var result = 0
        forEach {
            result += it.code
            result*=17
            result %= 256
        }
        return result
    }
    check("HASH".hashIt() == 52)


    fun part1(input: List<String>): Int {
        val single = input.single()
        return  single.split(",")
            .sumOf { it.hashIt()}

    }
    fun part2(input: List<String>): Int {
        val single = input.single()
        val map = mutableMapOf<Int, MutableMap<String, Int>>()
        val reg  = """([a-z]+)([=-])(\d*)""".toRegex()
        single.split(",")
            .forEach {
                val (word, operation, numberStr) = reg.matchEntire(it)!!.destructured
                val bucket = word.hashIt()
                val box = map.getOrPut(bucket) { mutableMapOf() }
                if(operation == "="){
                    val number = numberStr.toInt()
                    box[word] = number
                }else if (operation == "-"){
                    box.remove(word)
                }else{
                    throw RuntimeException(operation)
                }

            }

        return map.entries.sumOf {
            val (boxNum, values) = it
            values.values.mapIndexed {  index, i ->
                i * (index+1)
            }.map {
                it * (boxNum+1)
            }.sum()
        }
    }

    val testInput = readInput("Day15_test")
    val part1 = part1(testInput)
    check(part1 == 1320){
        part1
    }
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 494980)
    check(part2(input) == 247933)
}

