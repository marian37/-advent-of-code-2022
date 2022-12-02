import java.io.File

fun main() {
  val scores = mapOf("X" to 1, "Y" to 2, "Z" to 3)
  val opponentScores = mapOf("A" to 1, "B" to 2, "C" to 3)
  val gameScores = listOf(listOf(3, 6, 0), listOf(0, 3, 6), listOf(6, 0, 3))
  val results = mapOf("X" to 0, "Y" to 3, "Z" to 6)

  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    var sum = 0
    for (line in input) {
      val (opponent, me) = line.split(" ")
      val myScore = scores[me] ?: 0
      val opponentScore = opponentScores[opponent] ?: 0      
      val gameScore = gameScores.get(opponentScore-1).get(myScore-1)
      sum += myScore + gameScore
    }
    return sum
  }

  fun part2(input: List<String>): Int {
    var sum = 0
    for (line in input) {
      val (opponent, res) = line.split(" ")
      val expectedScore = results[res] ?: 0
      val opponentScore = opponentScores[opponent] ?: 0      
      val myScore = gameScores.get(opponentScore-1).indexOf(expectedScore) + 1
      sum += myScore + expectedScore
    }
    return sum
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("02_test")
  check(part1(testInput) == 15)
  check(part2(testInput) == 12)

  val input = readInput("02")
  println(part1(input))
  println(part2(input))
}
