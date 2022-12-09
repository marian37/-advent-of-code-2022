import java.io.File
import kotlin.math.abs

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun getNewTPos(oldTPos: Pair<Int, Int>, hPos: Pair<Int, Int>): Pair<Int, Int> {
    val fDiff = abs(hPos.first - oldTPos.first)
    val sDiff = abs(hPos.second - oldTPos.second)
    if (fDiff <= 1 && sDiff <= 1) return oldTPos

    if (fDiff > 1) {
      if (sDiff == 0) return Pair((oldTPos.first + hPos.first) / 2, oldTPos.second)
      if (sDiff == 2) return Pair((oldTPos.first + hPos.first) / 2, (oldTPos.second + hPos.second) / 2)
      return Pair((oldTPos.first + hPos.first) / 2, hPos.second)
    }

    if (fDiff == 0) return Pair(oldTPos.first, (oldTPos.second + hPos.second) / 2)
    if (fDiff == 2) return Pair((oldTPos.first + hPos.first) / 2, (oldTPos.second + hPos.second) / 2)
    return Pair(hPos.first, (oldTPos.second + hPos.second) / 2)
  }

  fun part1(input: List<String>): Int {
    val directions = mapOf("R" to Pair(0, 1), "L" to Pair(0, -1), "U" to Pair(1, 0), "D" to Pair(-1, 0))
    var hPos = Pair(0, 0)
    var tPos = Pair(0, 0)
    val tPositions = mutableSetOf<Pair<Int, Int>>()
    for (line in input) {
      val (dir, len) = line.split(" ")
      val length = len.toInt()
      val d = directions[dir]!!
      repeat(length) {
        hPos = Pair(hPos.first + d.first, hPos.second + d.second)
        tPos = getNewTPos(tPos, hPos)        
        tPositions.add(tPos)
      }
    }    
    return tPositions.size
  }

  fun part2(input: List<String>): Int {
    val directions = mapOf("R" to Pair(0, 1), "L" to Pair(0, -1), "U" to Pair(1, 0), "D" to Pair(-1, 0))
    var pos = MutableList(10) { Pair(0, 0) }    
    val tPositions = mutableSetOf<Pair<Int, Int>>()
    for (line in input) {
      val (dir, len) = line.split(" ")
      val length = len.toInt()
      val d = directions[dir]!!
      repeat(length) {
        pos[0] = Pair(pos[0].first + d.first, pos[0].second + d.second)
        for (i in 1..9) pos[i] = getNewTPos(pos[i], pos[i-1])        
        tPositions.add(pos[9])
      }
    }    
    return tPositions.size
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("09_test")
  check(part1(testInput) == 13)
  check(part2(testInput) == 1)
  val testInput2 = readInput("09_test2")
  check(part2(testInput2) == 36)

  val input = readInput("09")
  println(part1(input))
  println(part2(input))
}
