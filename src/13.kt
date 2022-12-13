import java.io.File

sealed class Packet()

data class PNode(val value: Int): Packet() {
  override fun toString(): String = "$value"
}

data class PList(val children: List<Packet>): Packet() {
  override fun toString(): String = "[${children.joinToString(", ")}]"
}

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parse(line: String): Packet {
    var i = 0
    var curr = ""
    val stack = ArrayDeque<MutableList<Packet>>()
    var lastItem = mutableListOf<Packet>()
    while (i < line.length) {
      when (line[i]) {
        '[' -> {
          stack.addLast(mutableListOf())
          i++
        }
        ']' -> {
          if (curr != "") {
            stack.last().add(PNode(curr.toInt()))
            curr = ""
          }
          val list = stack.removeLast()
          lastItem = list
          if (!stack.isEmpty()) stack.last().add(PList(list))
          i++
        }
        ',' -> {
          if (curr != "") {
            stack.last().add(PNode(curr.toInt()))
            curr = ""
          }
          i++
        }
        else -> {
          curr += line[i]
          i++
        }
      }
    }
    return PList(lastItem)
  }

  fun compare(first: Packet, second: Packet): Int {    
    if (first is PNode) {
      if (second is PNode) return second.value - first.value
      else return compare(PList(listOf(first)), second)
    } 
    if (first is PList) {
      if (second is PList) {
        var i = 0
        while(true) {          
          if (i == first.children.size) return if (i == second.children.size) 0 else 1
          if (i == second.children.size) return -1
          val res = compare(first.children[i], second.children[i])
          if (res != 0) return res
          i++
        }
      }
      else return compare(first, PList(listOf(second)))
    }
    return 0
  }

  fun part1(input: List<String>): Int {
    var i = 1
    var sum = 0
    for (packets in input.windowed(2, 3)) {
      val first = parse(packets[0])
      val second = parse(packets[1])
      if (compare(first, second) > 0) sum += i
      i++
    }    
    return sum
  }

  fun part2(input: List<String>): Int {
    val distressSignal = listOf("[[2]]", "[[6]]")
    val extendedInput = input.filter {it != ""} + distressSignal
    val inputPackets = extendedInput.map { parse(it) }
    val sortedPackets = inputPackets.sortedWith { a, b -> -compare(a, b)}    
    return distressSignal.map { sortedPackets.indexOf(parse(it)) + 1 }.reduce {acc, i -> acc * i}
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("13_test")
  check(part1(testInput) == 13)
  check(part2(testInput) == 140)

  val input = readInput("13")
  println(part1(input))
  println(part2(input))
}
