import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseStacks(input: List<String>): List<ArrayDeque<Char>> {
    val numberOfStacks = (input[0].length + 1) / 4
    val stacks = List<ArrayDeque<Char>>(numberOfStacks) { ArrayDeque() }    
    input.forEach { line ->      
      var i = 0        
      for (crate in line.windowed(3, 4)) {
        if (crate[1] != ' ') stacks[i].addFirst(crate[1])
        i++
      }
    }
    return stacks
  }

  data class Instruction(val quantity: Int, val from: Int, val to: Int)

  fun parseInstructions(input: List<String>): List<Instruction> {
    val instructions = mutableListOf<Instruction>()
    val regex = Regex("""move (\d+) from (\d+) to (\d+)""")
    for (line in input) {
      regex.matchEntire(line)!!.destructured.let { (quantity, from, to) ->
        instructions.add(Instruction(quantity.toInt(), from.toInt() - 1, to.toInt() - 1))
      }
    }
    return instructions
  }

  fun moveOneAtTime(stacks: List<ArrayDeque<Char>>, move: Instruction): Unit {
    repeat(move.quantity) {
      val moving = stacks[move.from].removeLast()
      stacks[move.to].addLast(moving)
    }
  }

  fun moveAll(stacks: List<ArrayDeque<Char>>, move: Instruction): Unit {
    val tempStack = ArrayDeque<Char>()
    repeat(move.quantity) {
      val moving = stacks[move.from].removeLast()
      tempStack.addLast(moving)
    }
    repeat(move.quantity) {
      val moving = tempStack.removeLast()
      stacks[move.to].addLast(moving)
    }
  }

  fun solution(input: List<String>, part1: Boolean): String {
    val divider = input.indexOf("")
    val stacks = parseStacks(input.slice(0..divider-1))
    val instructions = parseInstructions(input.slice(divider+1..input.lastIndex))
    for (move in instructions) {
      if (part1) moveOneAtTime(stacks, move) else moveAll(stacks, move)
    }
    return stacks.joinToString (separator = "") { stack -> "${stack.last()}" }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("05_test")
  check(solution(testInput, true) == "CMZ")
  check(solution(testInput, false) == "MCD")

  val input = readInput("05")
  println(solution(input, false))
  println(solution(input, true))
}