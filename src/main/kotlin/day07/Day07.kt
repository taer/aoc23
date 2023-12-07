package day07

import println
import readInput
import sumOfIndexed

fun main() {
    class Hand(val cards: String, val bid: Int){

        fun type(joker: Boolean): Int{
            val jokers = if(joker){
                cards.count { it=='J' }
            }else{
                0
            }
            if(jokers == 5){
                return 7
            }
            val groupBy = cards.groupBy { it }
                .filterNot { joker && it.key == 'J' }
                .mapValues { it.value.size }
                .values.sortedDescending()
            return when (groupBy[0] + jokers){
                5 -> 7
                4 -> 6
                3 -> {
                    if(groupBy[1]==2){
                        5 //full house
                    }else{
                        4
                    }
                }
                2 -> {
                    if (groupBy[1]==2){
                        3 //2 pair
                    }else{
                        2
                    }
                }
                1 -> 1

                else -> {0}
            }

        }
    }


    val cardz1 = buildMap {
        (2..9).forEach {
            put(it.toString()[0], it)
        }
        put('T', 10)
        put('J', 11)
        put('Q', 12)
        put('K', 13)
        put('K', 14)
        put('A', 15)
    }

    fun handComparator(cardValue: Map<Char, Int>, joker: Boolean) = compareBy<Hand> { it.type(joker) }
        .thenComparing { o1, o2 ->
            val zip = o1.cards.zip(o2.cards)
            val find = zip.find { it.first != it.second }
            if (find == null) {
                0//all the same
            } else {
                compareValues(
                    cardValue.getValue(find.first),
                    cardValue.getValue(find.second),
                )
            }
        }


    fun parseInput(input: List<String>) = input.map {
        val (l,r) = it.split(" ", limit = 2)
        Hand(l, r.toInt())
    }

    fun sortAndSum(input: List<String>, comparator: Comparator<Hand>): Int {
        val data = parseInput(input)
        val sorted = data.sortedWith(comparator)
        return sorted
            .sumOfIndexed {idx, hand ->
                hand.bid * (idx+1)
            }
    }
    fun part1(input: List<String>): Int {
        return sortAndSum(input, handComparator(cardz1, joker = false))
    }

    fun part2(input: List<String>): Int {
        val jokerCards = cardz1.toMutableMap()
            .let {
                it['J'] = 1
                it.toMap()
            }
        return sortAndSum(input, handComparator(jokerCards, joker = true))
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
    check(part1(input) == 251058093)
    check(part2(input) == 249781879)
}
