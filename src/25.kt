import java.io.File
import kotlin.math.pow

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val powersOf5 = mutableListOf(1L)
  for (i in 1..25) powersOf5.add(powersOf5.last() * 5L)

  val snafuDigits = mapOf('2' to 2L, '1' to 1L, '0' to 0L, '-' to -1L, '=' to -2L)

  fun snafuToDecimal(snafu: String): Long {
    var temp = 0L
    for (i in snafu.indices) {
      val c = snafu[snafu.length - 1 - i]
      temp += snafuDigits[c]!! * powersOf5[i]
    }
    return temp
  }

  fun decimalToSnafu(decimal: Long): String {
    var temp = MutableList<Int?>(powersOf5.size) { null }
    var number = decimal
    while (number > 0) {
      val idx = powersOf5.indexOfLast { it <= number }
      temp[idx] = (number / powersOf5[idx]).toInt()
      number = number % powersOf5[idx]
      // println("$idx $number ${number / powersOf5[idx]}")
    }
    val lastIdx = temp.indexOfLast { it != null }
    for (i in 0..lastIdx) { if (temp[i] == null) temp[i] = 0 }
    // println(temp)
    for (i in temp.indices) {
      when(temp[i]) {
        3 -> {
          temp[i] = -2
          temp[i+1] = (temp[i+1] ?: 0) + 1
        }
        4 -> {
          temp[i] = -1
          temp[i+1] = (temp[i+1] ?: 0) + 1
        }
        5 -> {
          temp[i] = 0
          temp[i+1] = (temp[i+1] ?: 0) + 1
        }
      }
    }
    // println(temp)
    return temp.map { 
      when(it) {
        -2 -> "="
        -1 -> "-"
        0 -> "0"
        1 -> "1"
        2 -> "2"
        else -> ""
      }
    }.joinToString("").reversed()
  }

  fun solution(input: List<String>): String {
    val sum = input.map { snafuToDecimal(it) }.sum()
    return decimalToSnafu(sum)
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("25_test")
  check(solution(testInput) == "2=-1=0")

  val input = readInput("25")
  println(solution(input))
}
