import java.io.File
import kotlin.math.abs

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val cycles = listOf(20, 60, 100, 140, 180, 220)

  fun checkSignal(registerX: Int, cycle: Int): Int {    
    if (cycle in cycles) {
      return registerX * cycle
    }

    return 0
  }

  fun part1(input: List<String>): Int {
    var registerX = 1
    var cycle = 1
    var signal = 0
    for (line in input) {
      signal += checkSignal(registerX, cycle)        
      if (line == "noop") cycle++
      else {
        cycle++
        signal += checkSignal(registerX, cycle)
        registerX += line.split(" ")[1].toInt()
        cycle++
      }
    }    
    return signal
  }

  fun drawPixel(res: MutableList<String>, curr: String, registerX: Int, cycle: Int): String {
    var c = curr    
    c += if (abs(registerX - (cycle-1) % 40) < 2) '#' else '.'
    if (cycle % 40 == 0) {
      res.add(c)
      return ""
    }
    return c
  }

  fun part2(input: List<String>): List<String> {
    var registerX = 1
    var cycle = 1
    val res = mutableListOf<String>()
    var curr = ""
    for (line in input) {      
      curr = drawPixel(res, curr, registerX, cycle)
      if (line == "noop") cycle++
      else {
        cycle++
        curr = drawPixel(res, curr, registerX, cycle)
        registerX += line.split(" ")[1].toInt()
        cycle++
      }
    }
    return res
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("10_test")
  check(part1(testInput) == 13140)
  part2(testInput).forEach{println(it)}

  val input = readInput("10")
  println(part1(input))
  part2(input).forEach{println(it)}
}