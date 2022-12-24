import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val directions = mapOf('^' to Pair(-1, 0), 'v' to Pair(1, 0), '<' to Pair(0, -1), '>' to Pair(0, 1))

  fun parseInput(input: List<String>): List<List<List<Char>>> {
    return input.map { line -> line.map { c -> if (c == '.') emptyList() else listOf(c) } }
  }

  fun simulateMinute(prev: List<List<List<Char>>>): List<List<List<Char>>> {
    // println("${prev.joinToString("\n")}\n")
    val next = List(prev.size) { List(prev[0].size) { mutableListOf<Char>() } }
    for (r in prev.indices) {
      for (c in prev[r].indices) {
        for (a in prev[r][c]) {
          when (a) {
            '#' -> next[r][c].add('#')
            else -> {
              val d = directions[a]!!
              var nextPos = Pair(r + d.first, c + d.second)
              if (nextPos.first == 0) nextPos = Pair(prev.lastIndex - 1, nextPos.second)
              if (nextPos.first == prev.lastIndex) nextPos = Pair(1, nextPos.second)
              if (nextPos.second == 0) nextPos = Pair(nextPos.first, prev[nextPos.first].lastIndex - 1)
              if (nextPos.second == prev[nextPos.first].lastIndex) nextPos = Pair(nextPos.first, 1)
              next[nextPos.first][nextPos.second].add(a)
            }
          }
        }
      }
    }
    return next
  }

  fun getPossiblePositions(valley: List<List<List<Char>>>, prev: Set<Pair<Int, Int>>): Set<Pair<Int, Int>> {
    val next = mutableSetOf<Pair<Int, Int>>()
    for (p in prev) {
      if (valley[p.first][p.second].isEmpty()) next.add(p)
      for (d in directions.values) {
        val np = Pair(p.first + d.first, p.second + d.second)
        if (np.first >= 0 && np.first <= valley.lastIndex && np.second >= 0 && np.second <= valley[0].lastIndex && valley[np.first][np.second].isEmpty())
          next.add(np)
      }
    }
    return next
  }

  fun part1(input: List<String>): Int {
    var time = 0
    var valley = parseInput(input)
    val start = Pair(0, input[0].indexOf('.'))
    val end = Pair(input.lastIndex, input[input.lastIndex].indexOf('.'))
    var positions = setOf(start)
    do {
      valley = simulateMinute(valley)
      time++
      positions = getPossiblePositions(valley, positions)
    } while (!positions.contains(end))
    return time
  }

  fun part2(input: List<String>): Int {
    var time = 0
    var valley = parseInput(input)
    val start = Pair(0, input[0].indexOf('.'))
    val end = Pair(input.lastIndex, input[input.lastIndex].indexOf('.'))
    var positions = setOf(start)
    do {
      valley = simulateMinute(valley)
      time++
      positions = getPossiblePositions(valley, positions)
    } while (!positions.contains(end))
    positions = setOf(end)
    do {
      valley = simulateMinute(valley)
      time++
      positions = getPossiblePositions(valley, positions)
    } while (!positions.contains(start))
    positions = setOf(start)
    do {
      valley = simulateMinute(valley)
      time++
      positions = getPossiblePositions(valley, positions)
    } while (!positions.contains(end))
    return time
  }

  // test if implementation meets criteria from the description, like:
  val testInput1 = readInput("24_test1")
  check(part1(testInput1) == 10)
  check(part2(testInput1) == 30)

  val testInput2 = readInput("24_test2")
  check(part1(testInput2) == 18)
  check(part2(testInput2) == 54)

  val input = readInput("24")
  println(part1(input))
  println(part2(input))
}
