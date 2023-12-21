package day20

import println
import readInput
import java.util.LinkedList

enum class State(val number: Int) {
    LOW(0), HIGH(1);

    fun inverted() = if(this==LOW) HIGH else LOW
}
sealed class Component{
    fun pulseIt(from: String, state: State): State? {
        return acceptPulse(from, state)
    }
    abstract val destinations: List<String>
    protected abstract fun acceptPulse(from: String, state: State): State?

    object Terminator:Component() {
        override val destinations: List<String>
            get() = listOf()

        var counts = 0L
        fun reset() {
            counts=0L
        }

        override fun acceptPulse(from: String, state: State): State? {
            counts++
            return null
        }
    }
    data class Broadcaster(override val destinations: List<String>):Component() {
        override fun acceptPulse(from: String, state: State): State {
            return state
        }
    }
    data class FlipFlop(override val destinations: List<String>):Component() {
        private var on = false
        override fun acceptPulse(from: String, state: State): State? {
            return if(state==State.HIGH){
                null
            }else{
                on = !on
                if(on)
                    State.HIGH
                else
                    State.LOW
            }
        }
    }

    data class Conjunction(val inputs: List<String>, override val destinations: List<String>):Component(){
        private val theState: MutableMap<String, State> = mutableMapOf<String, State>()
        init {
            inputs.forEach { theState[it]=State.LOW }
        }
        override fun acceptPulse(from: String, state: State): State {
            theState[from] = state
            return  if(theState.values.all { it==State.HIGH }){
                State.LOW
            }else{
                State.HIGH
            }
        }
    }
}

fun main() {



    fun parseTree(input: List<String>): Map<String, Component> {

        val theData = input.associate {

            val (name, destinationStr) = it.split(" -> ")
            val destinations = destinationStr.split(", ")
            val node = if(name=="broadcaster")
                Component.Broadcaster(destinations)
            else if (name.startsWith("%"))
                Component.FlipFlop(destinations)
            else if (name.startsWith("&"))
                Component.Conjunction(listOf(),destinations)
            else throw RuntimeException(name)
            name.trimStart('%','&') to node
        }


        return theData
    }

    fun doTheParse(input: List<String>):  Map<String, Component> {
        val halfParse = parseTree(input).toMutableMap()
        val conjunctions = halfParse.entries.filter { it.value is Component.Conjunction }.map { it.key }
        conjunctions.forEach { conjName ->
            val targets = input.filter { it.substringAfter(" -> ").split(", ").contains(conjName) }.map { it.trimStart('%', '&').substringBefore(" ->") }
            val current = halfParse.getValue(conjName) as Component.Conjunction
            halfParse[conjName] = current.copy(inputs = targets)
        }
        val finalParse = halfParse.toMap()
        return finalParse
    }

    fun part1(input: List<String>, times: Int=1000, debug: Boolean = false): Long {
        data class Work(val source: String, val pulseToSend: State, val toSendTo:String)
        val finalParse = doTheParse(input)

        val stateSent = Array(2){0L}
        repeat(times){
            if(debug) println()
            val queue = LinkedList<Work>()
            queue.add(Work("button", State.LOW, "broadcaster"))
            while(queue.isNotEmpty()){
                val (source, stateToSend, target) = queue.removeFirst()
                val component = finalParse[target]
                if(debug) println("$source sent $stateToSend to $target")
                stateSent[stateToSend.number]++
                if(component!=null){
                    val resultPulse = component.pulseIt(source,stateToSend)
                    if(resultPulse!=null){
                        component.destinations.forEach {
                            queue.add(Work(target, resultPulse, it))
                        }
                    }
                }
            }
        }


       return stateSent[0] * stateSent[1]
    }


    fun part2(input: List<String>, times: Int=1000, debug: Boolean = false): Long {
        data class Work(val source: String, val pulseToSend: State, val toSendTo:String)
        val finalParse = doTheParse(input).also {
            val toMutableMap = it.toMutableMap()
            toMutableMap["rx"] = Component.Terminator
            toMutableMap.toMap()
        }

        var x=0L
        while(true){
            x++
            Component.Terminator.reset()
            if(debug) println()
            val queue = LinkedList<Work>()
            queue.add(Work("button", State.LOW, "broadcaster"))
            while(queue.isNotEmpty()){
                val (source, stateToSend, target) = queue.removeFirst()
                if(debug) println("$source sent $stateToSend to $target")
                val component = finalParse[target]
                if(component!=null){
                    val resultPulse = component.pulseIt(source,stateToSend)
                    if(resultPulse!=null){
                        component.destinations.forEach {
                            queue.add(Work(target, resultPulse, it))
                        }
                    }
                }
            }
            if(Component.Terminator.counts !=0L){

                println(Component.Terminator.counts)
            }
            if(Component.Terminator.counts==1L){
                return x
            }
        }

    }

    val reddit = """
        broadcaster -> a
        &a -> output
    """.trimIndent().lines()
    val reddita = part1(reddit, debug = true)
    check(reddita == 2000*1000L) {
        reddita
    }

    val testInput = readInput("Day20_test")
    val part1 = part1(testInput)
    check(part1 == 32000000L) {
        part1
    }
    val testInput2 = readInput("Day20_test2")
    val part1a = part1(testInput2)
    check(part1a == 11687500L) {
        part1a
    }
//    val part2 = part2(testInput)
//    check(part2 == 167409079868000) {
//        part2
//    }

    val input = readInput("Day20")
    part1(input, debug = true).println()
    part2(input).println()
    check(part1(input) == 814934624L)
//    check(part2(input) == 138616621185978)
}

