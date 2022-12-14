import java.io.File
import kotlin.math.min
import kotlin.math.max

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val paths = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    for (line in input) {
      val points = line.split(" -> ")
      for ((s, e) in points.windowed(2, 1)) {
        val spx = s.substringBefore(",").toInt()
        val spy = s.substringAfter(",").toInt()
        val epx = e.substringBefore(",").toInt()
        val epy = e.substringAfter(",").toInt()
        paths.add(Pair(Pair(spx, spy), Pair(epx, epy)))
      }
    }
    return paths
  }

  fun prepareGrid(paths: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, height: Int, width: Int, minX: Int): MutableList<MutableList<Char>> {
    val grid = MutableList(height) { MutableList(2*width + 1) { '.' } }
    for (path in paths) {
      if (path.first.first == path.second.first) {
        val a = path.first.second
        val b = path.second.second
        for (i in min(a,b)..max(a,b)) grid[i][path.first.first - minX] = '#'
      } else {
        val a = path.first.first
        val b = path.second.first
        for (i in min(a,b)..max(a,b)) grid[path.first.second][i - minX] = '#'
      }      
    }
    return grid
  }  

  fun getNext(grid: MutableList<MutableList<Char>>, current: Pair<Int, Int>): Pair<Int, Int>? {
    if (current.second + 1 == grid.size) return null
    if (grid[current.second + 1][current.first] == '.') return Pair(current.first, current.second + 1)
    if (grid[current.second + 1][current.first - 1] == '.') return Pair(current.first - 1, current.second + 1)
    if (grid[current.second + 1][current.first + 1] == '.') return Pair(current.first + 1, current.second + 1)
    return null
  }

  fun simulate(grid: MutableList<MutableList<Char>>, sandSource: Pair<Int, Int>): Int {
    var current = sandSource
    var count = 0
    while (grid[sandSource.second][sandSource.first] != 'o') {
      grid[current.second][current.first] = '+'
      var next = getNext(grid, current)
      while(next != null && next.second < grid.size) {
        grid[current.second][current.first] = '.'
        current = next
        grid[current.second][current.first] = '+'
        next = getNext(grid, current)
      }      
      if (current.second + 1 == grid.size) break
      grid[current.second][current.first] = 'o'
      count++
      current = sandSource      
    }    
    return count
  }

  fun part1(input: List<String>): Int {
    val sandSource = Pair(500, 0)
    val paths = parseInput(input)
    val pathMinX = paths.minOf { min(it.first.first, it.second.first) }
    val pathMaxX = paths.maxOf { max(it.first.first, it.second.first) }
    val width = max(sandSource.first - pathMinX, pathMaxX - sandSource.first) + 1
    val height = paths.maxOf { max(it.first.second, it.second.second) } + 2
    val minX = sandSource.first - width

    val grid = prepareGrid(paths, height, width, minX)
    return simulate(grid, Pair(sandSource.first - minX, sandSource.second))
  }

  fun part2(input: List<String>): Int {
    val sandSource = Pair(500, 0)
    val paths = parseInput(input)
    val pathMinX = paths.minOf { min(it.first.first, it.second.first) }
    val pathMaxX = paths.maxOf { max(it.first.first, it.second.first) }
    val width = max(sandSource.first - pathMinX, pathMaxX - sandSource.first) * 4
    val height = paths.maxOf { max(it.first.second, it.second.second) } + 3
    val minX = sandSource.first - width

    val grid = prepareGrid(paths, height, width, minX)
    for (i in 0..grid[grid.lastIndex].lastIndex) grid[grid.lastIndex][i] = '#'
    return simulate(grid, Pair(sandSource.first - minX, sandSource.second))
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("14_test")
  check(part1(testInput) == 24)
  check(part2(testInput) == 93)

  val input = readInput("14")
  println(part1(input))
  println(part2(input))
}
