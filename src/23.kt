import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val adjacent = listOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1), Pair(0, 1), Pair(0, -1), Pair(1, -1), Pair(1, 0), Pair(1, 1))

  val directions = listOf(
    listOf(Pair(-1, 0), Pair(-1, -1), Pair(-1, 1)),
    listOf(Pair(1, 0), Pair(1, -1), Pair(1, 1)),
    listOf(Pair(0, -1), Pair(1, -1), Pair(-1, -1)),
    listOf(Pair(0, 1), Pair(1, 1), Pair(-1, 1)),
  )

  fun sumEmpty(grid: Map<Pair<Int, Int>, Int>): Int {
    val minR = grid.minOf { (k, _) -> k.first }
    val maxR = grid.maxOf { (k, _) -> k.first }
    val minC = grid.minOf { (k, _) -> k.second }
    val maxC = grid.maxOf { (k, _) -> k.second }
    val height = maxR - minR + 1
    val width = maxC - minC + 1
    return height * width - grid.size
  }

  fun parseInput(input: List<String>): Map<Pair<Int, Int>, Int> {
    val grid = mutableMapOf<Pair<Int, Int>, Int>()
    var count = 0
    for (r in input.indices) {
      for (c in input[r].indices) {
        if (input[r][c] == '#') {
          grid.put(Pair(r, c), count)
          count++
        }
      }
    }
    return grid
  }

  fun simulateRound(grid: Map<Pair<Int, Int>, Int>, firstDirection: Int): Map<Pair<Int, Int>, Int> {
    val proposals = mutableMapOf<Pair<Int, Int>, List<Int>>()
    for ((position, count) in grid) {
      val (r, c) = position
      var newR = r
      var newC = c
      if (!adjacent.all { grid[Pair(r + it.first, c + it.second)] == null }) {
        for (i in directions.indices) {
          val idx = (firstDirection + i) % directions.size
          if (directions[idx].all { grid[Pair(r + it.first, c + it.second)] == null }) {
            newR = r + directions[idx][0].first
            newC = c + directions[idx][0].second
            break
          }
        }
      }
      val pos = Pair(newR, newC)
      proposals.put(pos, proposals.getOrElse(pos) { emptyList() } + listOf(count))
    }
    // println(proposals)
    val res = mutableMapOf<Pair<Int, Int>, Int>()
    val toBeAdded = mutableSetOf<Int>()
    for ((k, v) in proposals) {
      if (v.size == 1) {
        res.put(k, v[0])
      } else {
        toBeAdded.addAll(v)
      }
    }
    for ((k, v) in grid) {
      if (v in toBeAdded) {
        res.put(k, v)
        toBeAdded.remove(v)
      }
    }
    // println(res)
    return res
  }

  fun part1(input: List<String>): Int {
    var grid = parseInput(input)
    var firstDirection = 0
    repeat(10) {
      grid = simulateRound(grid, firstDirection)
      firstDirection = (firstDirection + 1) % directions.size
    }
    return sumEmpty(grid)
  }

  fun part2(input: List<String>): Int {
    var grid = parseInput(input)
    var firstDirection = 0
    var round = 1
    while(true) {
      val newGrid = simulateRound(grid, firstDirection)
      firstDirection = (firstDirection + 1) % directions.size
      if (newGrid == grid) break
      grid = newGrid
      round++
    }
    return round
  }

  // test if implementation meets criteria from the description, like:
  val testInput1 = readInput("23_test1")
  check(part1(testInput1) == 25)
  check(part2(testInput1) == 4)

  val testInput2 = readInput("23_test2")
  check(part1(testInput2) == 110)
  check(part2(testInput2) == 20)

  val input = readInput("23")
  println(part1(input))
  println(part2(input))
}
