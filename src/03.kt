import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {
    var sum = 0
    for (line in input) {
      val half = line.length / 2
      val first = line.slice(0..half-1)
      val second = line.slice(half..line.length-1)
      for (c in first) {
        if (second.contains(c)) {          
          if (c <= 'Z') sum += c - 'A' + 27
          else sum += c - 'a' + 1
          break
        }
      }
    }
    return sum
  }

  fun part2(input: List<String>): Int {
    var sum = 0
    for (group in input.windowed(3, 3)) {
      for (c in group[0]) {
        if (group[1].contains(c) && group[2].contains(c)) {
          if (c <= 'Z') sum += c - 'A' + 27
          else sum += c - 'a' + 1
          break
        }
      }
    }
    return sum
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("03_test")
  check(part1(testInput) == 157)
  check(part2(testInput) == 70)

  val input = readInput("03")
  println(part1(input))
  println(part2(input))
}
