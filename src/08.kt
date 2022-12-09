import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    val size = input.size - 1
    val visible = MutableList(input.size) { MutableList(input[0].length) { false }}
    for (i in input.indices) {
      var highestR = '0' - 1
      var highestC = '0' - 1
      var highestRr = '0' - 1
      var highestCr = '0' - 1    
      for (j in input.indices) {
        if (input[i].get(j) > highestR) {
          highestR = input[i].get(j)
          visible[i][j] = true
        }
        if (input[j].get(i) > highestC) {
          highestC = input[j].get(i)
          visible[j][i] = true
        }
        if (input[size - i].get(size - j) > highestRr) {
          highestRr = input[size - i].get(size - j)
          visible[size - i][size - j] = true
        }
        if (input[size - j].get(size - i) > highestCr) {
          highestCr = input[size - j].get(size - i)
          visible[size - j][size - i] = true
        }
      }
    }
    return visible.sumOf { it.count { it } }
  }

  fun countScenicScore(input: List<String>, startI: Int, startJ: Int): Int {
    var score = 1
    val size = input.size
    val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(-1, 0), Pair(1, 0))
    for (dir in directions) {
      var i = startI + dir.first
      var j = startJ + dir.second
      var dirScore = 0
      while (i >= 0 && i < size && j >= 0 && j < size) {
        if (input[i].get(j) >= input[startI].get(startJ)) {
          dirScore++
          break
        }

        dirScore++

        i += dir.first
        j += dir.second
      }
      score *= dirScore      
    }
    return score
  }

  fun part2(input: List<String>): Int {
    var maxScenicScore = 0
    for (i in 1..input.size - 2) {
      for (j in 1..input[0].length - 2) {
        val score = countScenicScore(input, i, j)        
        if (score > maxScenicScore) maxScenicScore = score
      }
    }
    return maxScenicScore
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("08_test")
  check(part1(testInput) == 21)
  check(part2(testInput) == 8)

  val input = readInput("08")
  println(part1(input))
  println(part2(input))
}
