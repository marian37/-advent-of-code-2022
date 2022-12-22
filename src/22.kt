import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

  fun move(repeatCount: Int, current: Triple<Int, Int, Int>, board: List<String>, rows: List<Pair<Int, Int>>, columns: List<Pair<Int, Int>>): Triple<Int, Int, Int> {    
    val dir = current.third
    val d = directions[dir]
    var y = current.first
    var x = current.second
    for(i in 1..repeatCount) {
      var newX: Int
      var newY: Int
      if (d.first == 0) {
        val row = rows[y]
        val rd = row.second - row.first + 1
        newY = y
        newX = (x + d.second - row.first + rd) % rd + row.first
      } else {
        val col = columns[x]
        val cd = col.second - col.first + 1
        newX = x
        newY = (y + d.first - col.first + cd) % cd + col.first
      }      
      if (board[newY][newX] == '.') {
        x = newX
        y = newY
      }
      if (board[newY][newX] == '#') break
    }
    return Triple(y, x, dir)
  }

  fun part1(input: List<String>): Int {
    val board = input.slice(0..input.size - 3)
    val pathDescription = input.last()
    var longest = 0
    val rows = board.map { line ->
      val first = line.indexOfFirst { it != ' ' }
      val last = line.indexOfLast { it != ' ' }
      if (line.length > longest) longest = line.length
      Pair(first, last)
    }
    val columns = (0 until longest).map { i ->
      val first = board.indexOfFirst { 
        val c = it.getOrNull(i)
        c != null && c != ' '
      }
      val last = board.indexOfLast { 
        val c = it.getOrNull(i)
        c != null && c != ' '
      }
      Pair(first, last)
    }
    var current = Triple(0, rows[0].first, 0)
    var c = 0
    var buf = ""
    while (c < pathDescription.length) {
      when (pathDescription[c]) {
        'R' -> {
          val repeatCount = buf.toInt()
          buf = ""
          current = move(repeatCount, current, board, rows, columns)
          current = Triple(current.first, current.second, (current.third + 1) % 4)
        }
        'L' -> {
          val repeatCount = buf.toInt()
          buf = ""
          current = move(repeatCount, current, board, rows, columns)
          current = Triple(current.first, current.second, (current.third + 3) % 4)
        }
        else -> buf += pathDescription[c]
      }
      c++
    }
    if (buf != "") {
      val repeatCount = buf.toInt()
      current = move(repeatCount, current, board, rows, columns)
    }
    return (current.first + 1)*1000 + (current.second + 1)*4 + current.third
  }

  fun getNewCurrent(c: List<Pair<Int, Int>>, qubeSize: Int, y: Int, x: Int, dir: Int, newY: Int, newX: Int): Triple<Int, Int, Int> {
    // println("$y $x $dir $newY $newX")
    val origCornerIdx = c.indexOf(Pair(y / qubeSize * qubeSize, x / qubeSize * qubeSize))
    if (qubeSize == 4) {
      return when(origCornerIdx) {
        0 -> listOf(Triple(c[5].first + qubeSize - y - 1, c[5].second + x % qubeSize, 2), Triple(newY, newX, dir), Triple(c[2].first, c[2].second + y, 1), Triple(c[1].first, c[1].second + qubeSize - 1 - x % qubeSize, 1))[dir]
        1 -> listOf(Triple(newY, newX, dir), Triple(c[4].first + y % qubeSize, c[4].second + qubeSize - x - 1, 3), Triple(c[5].first + qubeSize - 1, c[5].first + qubeSize - 1 - y % qubeSize, 3), Triple(c[0].first, c[0].second + qubeSize - 1 - x % qubeSize, 1))[dir]
        2 -> listOf(Triple(newY, newX, dir), Triple(c[4].first + qubeSize - 1 - x % qubeSize, c[4].second, 0), Triple(newY, newX, dir), Triple(c[0].first + x % qubeSize, c[0].second, 0))[dir]
        3 -> listOf(Triple(c[5].first, c[5].second + qubeSize - 1 - y % qubeSize, 1), Triple(newY, newX, dir), Triple(newY, newX, dir), Triple(newY, newX, dir))[dir]
        4 -> listOf(Triple(newY, newX, dir), Triple(c[1].first + y % qubeSize, c[1].second + qubeSize - 1 - x % qubeSize, 3), Triple(c[2].first + x % qubeSize, c[2].second, 1), Triple(newY, newX, dir))[dir]
        else -> listOf(Triple(c[0].first + qubeSize - 1 - y % qubeSize, c[0].second + x % qubeSize, 2), Triple(c[1].first + x % qubeSize, c[1].second, 0), Triple(newY, newX, dir), Triple(c[3].first + qubeSize - 1 - x % qubeSize, c[3].second + qubeSize - 1, 2))[dir]
      }
    }
    return when(origCornerIdx) {
      0 -> listOf(Triple(newY, newX, dir), Triple(newY, newX, dir), Triple(c[3].first + qubeSize - 1 - y % qubeSize, c[3].second, 0), Triple(c[5].first + x % qubeSize, c[5].second, 0))[dir]
      1 -> listOf(Triple(c[4].first + qubeSize - 1 - y % qubeSize, c[4].second + x % qubeSize, 2), Triple(c[2].first + x % qubeSize, c[2].second + qubeSize - 1, 2), Triple(newY, newX, dir), Triple(c[5].first + qubeSize - 1, c[5].second + x % qubeSize, 3))[dir]
      2 -> listOf(Triple(c[1].first + qubeSize - 1, c[1].second + y % qubeSize, 3), Triple(newY, newX, dir), Triple(c[3].first, c[3].second + y % qubeSize, 1), Triple(newY, newX, dir))[dir]
      3 -> listOf(Triple(newY, newX, dir), Triple(newY, newX, dir), Triple(c[0].first + qubeSize - 1 - y % qubeSize, c[0].second, 0), Triple(c[2].first + x % qubeSize, c[2].second, 0))[dir]
      4 -> listOf(Triple(c[1].first + qubeSize - 1 - y % qubeSize, c[1].second + x % qubeSize, 2), Triple(c[5].first + x % qubeSize, c[5].second + qubeSize - 1, 2), Triple(newY, newX, dir), Triple(newY, newX, dir))[dir]
      else -> listOf(Triple(c[4].first + qubeSize - 1, c[4].second + y % qubeSize, 3), Triple(c[1].first, c[1].second + x % qubeSize, 1), Triple(c[0].first, c[0].second + y % qubeSize, 1), Triple(newY, newX, dir))[dir]
    }
  }

  fun move2(repeatCount: Int, current: Triple<Int, Int, Int>, board: List<String>, qubeSize: Int, corners: List<Pair<Int, Int>>): Triple<Int, Int, Int> {
    var dir = current.third
    var d = directions[dir]
    var y = current.first
    var x = current.second
    for(i in 1..repeatCount) {
      var newY = y + d.first
      var newX = x + d.second
      if (newY / qubeSize != y / qubeSize || newX / qubeSize != x / qubeSize || newY < 0 || newX < 0) {
        val newCurrent = getNewCurrent(corners, qubeSize, y, x, dir, newY, newX)
        if (board[newCurrent.first][newCurrent.second] == '#') return Triple(y, x, dir)
        return move2(repeatCount - i, newCurrent, board, qubeSize, corners)
      }
      if (board[newY][newX] == '.') {
        x = newX
        y = newY
      }
      if (board[newY][newX] == '#') break
    }
    return Triple(y, x, dir)
  }

  fun part2(input: List<String>): Int {
    val qubeSize = if (input.size > 50) 50 else 4
    val board = input.slice(0..input.size - 3)
    val pathDescription = input.last()
    var longest = 0
    val rows = board.map { line ->
      val first = line.indexOfFirst { it != ' ' }
      val last = line.indexOfLast { it != ' ' }
      if (line.length > longest) longest = line.length
      Pair(first, last)
    }
    val corners = mutableListOf<Pair<Int, Int>>()
    var i = 0
    while (i < board.size) {
      val r = rows[i]
      var j = r.first
      while (j < r.second) {
        corners.add(Pair(i, j))
        j += qubeSize
      }
      i += qubeSize
    }
    // println(corners)
    var current = Triple(0, rows[0].first, 0)
    var c = 0
    var buf = ""
    while (c < pathDescription.length) {
      when (pathDescription[c]) {
        'R' -> {
          val repeatCount = buf.toInt()
          buf = ""
          current = move2(repeatCount, current, board, qubeSize, corners)
          // println("POS: (${current.first}, ${current.second}) ${directions[current.third]}")
          current = Triple(current.first, current.second, (current.third + 1) % 4)
        }
        'L' -> {
          val repeatCount = buf.toInt()
          buf = ""
          current = move2(repeatCount, current, board, qubeSize, corners)
          // println("POS: (${current.first}, ${current.second}) ${directions[current.third]}")
          current = Triple(current.first, current.second, (current.third + 3) % 4)
        }
        else -> buf += pathDescription[c]
      }
      c++
    }
    if (buf != "") {
      val repeatCount = buf.toInt()
      current = move2(repeatCount, current, board, qubeSize, corners)
    }
    return (current.first + 1)*1000 + (current.second + 1)*4 + current.third
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("22_test")
  check(part1(testInput) == 6032)
  check(part2(testInput) == 5031)

  val input = readInput("22")
  println(part1(input))
  println(part2(input))
}
