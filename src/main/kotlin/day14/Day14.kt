package day14

import println
import readInput
import sumOfIndexed

fun main() {


    fun part1(input: List<String>): Int {
        val map = input.map { it.toMutableList() }.toMutableList()
        val columns = map.first().size
        for (row in 0 until map.size){
            for (col in 0 until columns) {
                var target = row
                if (map[row][col] == 'O') {
                    while (target > 0 && map[target - 1][col] == '.') {
                        target--
                    }
                    map[row][col] = '.'
                    map[target][col] = 'O'
                }
            }
        }
//        map.forEach {
//            println(it.joinToString(""))
//        }

        return map.sumOfIndexed { index, chars ->
            chars.count { it == 'O' } * (map.size - index)
        }
    }


    fun printTheMap(map: MutableList<MutableList<Char>>) {
        map.forEach {
            println(it.joinToString(""))
        }
        println()
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toMutableList() }.toMutableList()
        val columns = map.first().size

        val target = 1000000000
        var count = 0
        val cache = mutableMapOf<List<List<Char>>, Int>()
        while(true){
            val put = cache.put(map, count)
            if(put != null){
                val i = count - put
                val moddy = target % i
                if(count % i == moddy) break
            }

            count++

            for (row in 0 until map.size){
                for (col in 0 until columns) {
                    var target = row
                    if (map[row][col] == 'O') {
                        while (target > 0 && map[target - 1][col] == '.') {
                            target--
                        }
                        map[row][col] = '.'
                        map[target][col] = 'O'
                    }
                }
            }

            for (row in 0 until map.size){
                for (col in 0 until columns) {
                    var target = col
                    if (map[row][col] == 'O') {
                        while (target > 0 && map[row][target-1] == '.') {
                            target--
                        }
                        map[row][col] = '.'
                        map[row][target] = 'O'
                    }
                }
            }

            for (row in map.size -1 downTo  0){
                for (col in 0 until columns) {
                    var target = row
                    if (map[row][col] == 'O') {
                        while (target <map.size-1 && map[target + 1][col] == '.') {
                            target++
                        }
                        map[row][col] = '.'
                        map[target][col] = 'O'
                    }
                }
            }
            for (row in 0 until map.size){
                for (col in columns-1  downTo  0) {
                    var target = col
                    if (map[row][col] == 'O') {
                        while (target < columns-1 && map[row][target+1] == '.') {
                            target++
                        }
                        map[row][col] = '.'
                        map[row][target] = 'O'
                    }
                }
            }

        }


        return map.sumOfIndexed { index, chars ->
            chars.count { it == 'O' } * (map.size - index)
        }
    }

    val testInput = readInput("Day14_test")
    val part1 = part1(testInput)
    check(part1 == 136){
        part1
    }
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 109755)
    check(part2(input) == 90928)
}

