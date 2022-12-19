import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val neighbors = listOf(Triple(0, 0, 1), Triple(0, 1, 0), Triple(1, 0, 0), Triple(0, 0, -1), Triple(0, -1, 0), Triple(-1, 0, 0))

  fun part1(input: List<String>): Int {
    val cubes = input.map { 
      val (x, y, z) = it.split(",")
      Triple(x.toInt(), y.toInt(), z.toInt())
    }
    val maxX = cubes.maxOf { it.first }
    val minX = cubes.minOf { it.first }
    val maxY = cubes.maxOf { it.second }
    val minY = cubes.minOf { it.second }
    val maxZ = cubes.maxOf { it.third }
    val minZ = cubes.minOf { it.third }
    val space = MutableList(maxX - minX + 3) { MutableList(maxY - minY + 3) { MutableList(maxZ - minZ + 3) { false } } }
    cubes.forEach { (x, y, z) ->
      space[x-minX+1][y-minY+1][z-minZ+1] = true
    }
    var surface = 0
    for (x in 1 until space.lastIndex) {
      for (y in 1 until space[x].lastIndex) {
        for (z in 1 until space[x][y].lastIndex) {
          if (space[x][y][z]) {
            var sides = 6
            for (n in neighbors) {
              if (space[x + n.first][y + n.second][z + n.third]) sides--
            }
            surface += sides
          }
        }
      }
    }
    return surface
  }

  fun part2(input: List<String>): Int {
    val cubes = input.map { 
      val (x, y, z) = it.split(",")
      Triple(x.toInt(), y.toInt(), z.toInt())
    }
    val maxX = cubes.maxOf { it.first }
    val minX = cubes.minOf { it.first }
    val maxY = cubes.maxOf { it.second }
    val minY = cubes.minOf { it.second }
    val maxZ = cubes.maxOf { it.third }
    val minZ = cubes.minOf { it.third }
    val space = MutableList(maxX - minX + 3) { MutableList(maxY - minY + 3) { MutableList(maxZ - minZ + 3) { -1 } } }
    cubes.forEach { (x, y, z) ->
      space[x-minX+1][y-minY+1][z-minZ+1] = 1
    }
    val queue = ArrayDeque<Triple<Int, Int, Int>>()
    queue.addLast(Triple(0, 0, 0))
    while(queue.isNotEmpty()) {
      val curr = queue.removeFirst()
      for (n in neighbors) {
        val t = Triple(curr.first + n.first, curr.second + n.second, curr.third + n.third)
        if (t.first >= 0 && t.first < space.size && t.second >= 0 && t.second < space[t.first].size && t.third >= 0 && t.third < space[t.first][t.second].size)
          if (space[t.first][t.second][t.third] == -1) {
            space[t.first][t.second][t.third] = 0
            queue.addLast(t)
          }
      }
    }
    var surface = 0
    for (x in 1 until space.lastIndex) {
      for (y in 1 until space[x].lastIndex) {
        for (z in 1 until space[x][y].lastIndex) {
          if (space[x][y][z] == 1) {
            var sides = 0
            for (n in neighbors) {
              if (space[x + n.first][y + n.second][z + n.third] == 0) sides++
            }
            surface += sides
          }
        }
      }
    }
    return surface
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("18_test")
  check(part1(testInput) == 64)
  check(part2(testInput) == 58)

  val input = readInput("18")
  println(part1(input))
  println(part2(input))
}
