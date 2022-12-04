import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun part1(input: List<String>): Int {    
    val regex = Regex("""(\d+)-(\d+),(\d+)-(\d+)""")    
    return input.count{ line ->
      regex.matchEntire(line)!!.destructured.let { (sx1, sy1, sx2, sy2) ->
          val x1 = sx1.toInt()
          val x2 = sx2.toInt()
          val y1 = sy1.toInt()
          val y2 = sy2.toInt()          
          (x1 <= x2 && y1 >= y2) || (x2 <= x1 && y2 >= y1)
      }
    }
  }

  fun part2(input: List<String>): Int {
    val regex = Regex("""(\d+)-(\d+),(\d+)-(\d+)""")    
    return input.count{ line ->
      regex.matchEntire(line)!!.destructured.let { (sx1, sy1, sx2, sy2) ->
          val x1 = sx1.toInt()
          val x2 = sx2.toInt()
          val y1 = sy1.toInt()
          val y2 = sy2.toInt()          
          (x1 <= x2 && y1 >= y2) || (x2 <= x1 && y2 >= y1) || (x1 <= x2 && y1 >= x2) || (x2 <= x1 && y2 >= x1)
      }
    }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("04_test")
  check(part1(testInput) == 2)
  check(part2(testInput) == 4)

  val input = readInput("04")
  println(part1(input))
  println(part2(input))
}
