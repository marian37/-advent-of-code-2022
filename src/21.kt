import java.io.File

data class Monkey(val name: String, val operation: Triple<String, Char, String>?, var value: Long?)

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseMonkeys(input: List<String>): List<Monkey> {
    return input.map { line ->
      val secondPart = line.substringAfter(":").trim().split(" ")
      val operation = if (secondPart.size == 1) null else Triple(secondPart[0], secondPart[1][0], secondPart[2])
      val value = if (secondPart.size == 1) secondPart[0].toLong() else null
      Monkey(line.substringBefore(":"), operation, value)
    }
  }

  var monkeys: List<Monkey> = emptyList()

  fun Monkey.eval(): Long {
    val value = this.value
    if (value != null) return value

    val (f, op, s) = this.operation!!
    val first = monkeys.find { it.name == f }!!
    val second = monkeys.find { it.name == s }!!
    val res = when (op) {
      '+' -> first.eval() + second.eval()
      '-' -> first.eval() - second.eval()
      '*' -> first.eval() * second.eval()
      '/' -> first.eval() / second.eval()
      else -> 0
    }
    this.value = res
    return res
  }

  fun Monkey.eval2(): Long? {
    if (this.name == "humn") return null
    val value = this.value
    if (value != null) return value

    val (f, op, s) = this.operation!!
    val first = monkeys.find { it.name == f }!!
    val second = monkeys.find { it.name == s }!!
    val fe = first.eval2()
    val se = second.eval2()
    if (fe == null || se == null) {
      this.value = null
      return null
    }
    val res = when (op) {
      '+' -> fe + se
      '-' -> fe - se
      '*' -> fe * se
      '/' -> fe / se
      else -> 0
    }
    this.value = res
    return res
  }

  fun Monkey.expectValue(expected: Long) {
    if (this.name == "humn") {
      this.value = expected
      // println(expected)
      return
    }
    val (f, op, s) = this.operation!!
    val first = monkeys.find { it.name == f }!!
    val second = monkeys.find { it.name == s }!!
    val left = first.eval2()
    val right = second.eval2()
    // println("$expected $this $left $right $first $second")
    if (left == null) {
      val r = right!!
      first.value = when (op) {
        '+' -> expected - r
        '-' -> expected + r
        '*' -> expected / r
        '/' -> expected * r
        else -> 0L
      }
      first.expectValue(first.value!!)
    }
    if (right == null) {
      val l = left!!
      second.value = when (op) {
        '+' -> expected - l
        '-' -> l - expected
        '*' -> expected / l
        '/' -> expected * l
        else -> 0L
      }
      second.expectValue(second.value!!)
    }
  }

  fun part1(input: List<String>): Long {
    monkeys = parseMonkeys(input)
    return monkeys.find { it.name == "root" }!!.eval()
  }

  fun part2(input: List<String>): Long {
    monkeys = parseMonkeys(input)
    val root = monkeys.find { it.name == "root" }!!
    val (f, _, s) = root.operation!!
    val first = monkeys.find { it.name == f }!!
    val second = monkeys.find { it.name == s }!!
    val left = first.eval2()
    val right = second.eval2()
    // println("$left $right $first $second")
    if (left == null) first.expectValue(right!!)
    if (right == null) second.expectValue(left!!)
    return monkeys.find { it.name == "humn" }!!.value!!
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("21_test")
  check(part1(testInput) == 152L)
  check(part2(testInput) == 301L)

  val input = readInput("21")
  println(part1(input))
  println(part2(input))
}
