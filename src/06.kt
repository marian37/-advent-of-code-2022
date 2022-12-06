import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()[0]

  fun solution(input: String, windowSize: Int): Int {
    var i = windowSize
    for (window in input.windowed(windowSize, 1)) {      
      if (window.toSet().size == windowSize) {
        return i
      }
      i++
    }
    return i
  }  

  // test if implementation meets criteria from the description, like:
  val windowSize1 = 4
  check(solution("mjqjpqmgbljsphdztnvjfqwrcgsmlb", windowSize1) == 7)
  check(solution("bvwbjplbgvbhsrlpgdmjqwftvncz", windowSize1) == 5)
  check(solution("nppdvjthqldpwncqszvftbrmjlhg", windowSize1) == 6)
  check(solution("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", windowSize1) == 10)
  check(solution("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", windowSize1) == 11)  
  val windowSize2 = 14
  check(solution("mjqjpqmgbljsphdztnvjfqwrcgsmlb", windowSize2) == 19)
  check(solution("bvwbjplbgvbhsrlpgdmjqwftvncz", windowSize2) == 23)
  check(solution("nppdvjthqldpwncqszvftbrmjlhg", windowSize2) == 23)
  check(solution("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", windowSize2) == 29)
  check(solution("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", windowSize2) == 26)  

  val input = readInput("06")
  println(solution(input, windowSize1))
  println(solution(input, windowSize2))
}