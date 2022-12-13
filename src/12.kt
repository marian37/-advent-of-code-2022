import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(-1, 0), Pair(1, 0))

  fun part1(input: List<String>): Int {
    val distance = MutableList(input.size) { MutableList(input[0].length) { -1 }}
    val queue = ArrayDeque<Pair<Int, Int>>()
    var startingPosition = Pair(0, 0)
    var finalPosition = Pair(0, 0)

    for (i in input.indices) {
      val s = input[i].indexOf("S")
      if (s != -1) startingPosition = Pair(i, s)
      val e = input[i].indexOf("E")
      if (e != -1) finalPosition = Pair(i, e)
    }

    distance[startingPosition.first][startingPosition.second] = 0
    queue.addLast(startingPosition)
    while (!queue.isEmpty()) {
      val p = queue.removeFirst()
      val currentInput = input[p.first][p.second]
      val current = if (currentInput == 'S') 'a' else currentInput
      val neighbours = directions.map { Pair(p.first + it.first, p.second + it.second) }
      for (q in neighbours) {
        if (q.first < 0 || q.first >= input.size || q.second < 0 || q.second >= input[0].length) continue
        val neighbourInput = input[q.first][q.second]
        val neighbour = if (neighbourInput == 'E') 'z' else neighbourInput
        if (distance[q.first][q.second] == -1 && neighbour - current < 2) {
          distance[q.first][q.second] = distance[p.first][p.second] + 1
          queue.addLast(q)
        }
      }
    }    
    return distance[finalPosition.first][finalPosition.second]
  }

  fun part2(input: List<String>): Int {
    val distance = MutableList(input.size) { MutableList(input[0].length) { -1 }}
    val queue = ArrayDeque<Pair<Int, Int>>()
    var startingPosition = Pair(0, 0)

    for (i in input.indices) {
      val s = input[i].indexOf("E")
      if (s != -1) startingPosition = Pair(i, s)
    }

    distance[startingPosition.first][startingPosition.second] = 0
    queue.addLast(startingPosition)
    while (!queue.isEmpty()) {
      val p = queue.removeFirst()
      val currentInput = input[p.first][p.second]
      val current = if (currentInput == 'E') 'z' else currentInput
      if (current == 'a' || current == 'S') {        
        return distance[p.first][p.second]
      }
      val neighbours = directions.map { Pair(p.first + it.first, p.second + it.second) }
      for (q in neighbours) {
        if (q.first < 0 || q.first >= input.size || q.second < 0 || q.second >= input[0].length) continue
        val neighbourInput = input[q.first][q.second]
        val neighbour = if (neighbourInput == 'S') 'a' else neighbourInput
        if (distance[q.first][q.second] == -1 && current - neighbour < 2) {
          distance[q.first][q.second] = distance[p.first][p.second] + 1
          queue.addLast(q)
        }
      }
    }    
    return -1
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("12_test")
  check(part1(testInput) == 31)
  check(part2(testInput) == 29)

  val input = readInput("12")
  println(part1(input))
  println(part2(input))
}
