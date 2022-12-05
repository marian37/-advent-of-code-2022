import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): String {
    val numberOfStacks = (input[0].length + 1) / 4
    val stacks = List<ArrayDeque<Char>>(numberOfStacks) { ArrayDeque() }
    var readingStacks = true
    input.forEach { line ->
      if (line == "") {
        readingStacks = false
        return@forEach
      }

      if (readingStacks) {
        var i = 0
        if (line[1] == '1') return@forEach
        for (crate in line.windowed(3, 4)) {
          if (crate[1] != ' ') stacks[i].addFirst(crate[1])
          i++
        }
        return@forEach
      }

      val regex = Regex("""move (\d+) from (\d+) to (\d+)""")
      regex.matchEntire(line)!!.destructured.let { (amount, from, to) ->
        repeat(amount.toInt()) {
          val moving = stacks[from.toInt()-1].removeLast()
          stacks[to.toInt()-1].addLast(moving)
        }
      }
    }    
    return stacks.joinToString (separator = "") { stack -> "${stack.last()}" }
  }

  fun part2(input: List<String>): String {
    val numberOfStacks = (input[0].length + 1) / 4
    val stacks = List<ArrayDeque<Char>>(numberOfStacks) { ArrayDeque() }
    var readingStacks = true
    input.forEach { line ->
      if (line == "") {
        readingStacks = false
        return@forEach
      }

      if (readingStacks) {
        var i = 0
        if (line[1] == '1') return@forEach
        for (crate in line.windowed(3, 4)) {
          if (crate[1] != ' ') stacks[i].addFirst(crate[1])
          i++
        }
        return@forEach
      }

      val regex = Regex("""move (\d+) from (\d+) to (\d+)""")
      regex.matchEntire(line)!!.destructured.let { (amount, from, to) ->
        val tempStack = ArrayDeque<Char>()
        repeat(amount.toInt()) {
          val moving = stacks[from.toInt()-1].removeLast()
          tempStack.addLast(moving)
        }
        repeat(amount.toInt()) {
          val moving = tempStack.removeLast()
          stacks[to.toInt()-1].addLast(moving)
        }
      }
    }    
    return stacks.joinToString (separator = "") { stack -> "${stack.last()}" }    
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("05_test")
  check(part1(testInput) == "CMZ")
  check(part2(testInput) == "MCD")

  val input = readInput("05")
  println(part1(input))
  println(part2(input))
}