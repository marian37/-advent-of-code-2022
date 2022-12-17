import java.io.File

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines().first()

  val rocks = listOf(
    listOf(listOf(true, true, true, true)),
    listOf(listOf(false, true, false), listOf(true, true, true), listOf(false, true, false)), 
    listOf(listOf(false, false, true), listOf(false, false, true), listOf(true, true, true)), 
    listOf(listOf(true), listOf(true), listOf(true), listOf(true)), 
    listOf(listOf(true, true), listOf(true, true))
  )

  fun canMove(row: Int, left: Int, chamber: List<List<Boolean>>, rock: List<List<Boolean>>): Boolean {
    if (row == 0) return false
    for (r in rock.indices) {
      val line = rock[rock.size - r - 1]
      val chamberLine = chamber[row + r]
      val chamberLinePart = chamberLine.slice(left until left + line.size)
      for (i in line.indices) {
        if (line[i] && chamberLinePart[i]) return false
      }
    }
    return true
  }

  fun updateChamber(row: Int, left: Int, chamber: MutableList<MutableList<Boolean>>, chamberHighest: MutableList<Int>, rock: List<List<Boolean>>) {
    for (r in rock.indices) {
      for (l in left until left + rock[0].size) {
        chamber[row + r][l] = chamber[row + r][l] || rock[rock.size - r - 1][l - left]
        if (chamber[row + r][l]) chamberHighest[l] = maxOf(row + r, chamberHighest[l])
      }
    }
  }

  val seq = MutableList(0) { "" }

  fun part1(input: String, count: Int, calcSequence: Boolean): Int {
    val chamber = MutableList(count * 4) { MutableList(7) { false } }
    val chamberHighest = MutableList(7) {0}
    var j = 0
    repeat(count) { i ->
      if (calcSequence && i % rocks.size == 0) seq.add("")
      val rock = rocks[i % rocks.size]
      var left = 2
      var bottom = chamberHighest.max() + 4
      var landed = false
      while(!landed) {
        // push
        val move = input[j % input.length]
        if (move == '>') {
          if (left + rock[0].size < chamber[0].size) 
            if (canMove(bottom, left+1, chamber, rock)) left++ 
        } else {
          if (left > 0) 
            if (canMove(bottom, left-1, chamber, rock)) left--
        }
        j++
        // fall down
        if (!canMove(bottom - 1, left, chamber, rock)) {
          if (calcSequence) seq[seq.lastIndex] = seq[seq.lastIndex] + left
          landed = true
          updateChamber(bottom, left, chamber, chamberHighest, rock)
        }
        else bottom--
      }
    }
    // println(chamber.reversed().joinToString("\n") { it.joinToString("") { if (it) "#" else "." } })
    return chamberHighest.max()
  }

  fun floydWarshallCycleDetection(): Pair<Int, Int> {
    var tId = 1
    var hId = 2
    while (seq[tId] != seq[hId] || seq[tId+1] != seq[hId+1]) {
      tId += 1
      hId += 2
    }

    var mu = 0
    tId = 0
    while(seq[tId] != seq[hId] || seq[tId+1] != seq[hId+1]) {
      tId += 1
      hId += 1
      mu++
    }

    var lam = 1
    hId = tId+1
    while(seq[tId] != seq[hId] || seq[tId+1] != seq[hId+1]) {
      hId += 1
      lam++
    }

    return Pair(mu, lam)
  }

  fun part2(input: String): Long {
    seq.clear()
    val COUNT = 10000
    part1(input, COUNT, true)
    val (mu, lam) = floydWarshallCycleDetection()
    // println(seq.drop(mu).take(3*lam).windowed(lam, lam).joinToString("\n"))
    // println("$mu $lam")
    val rs = rocks.size
    val muHeight = part1(input, mu*rs, false)
    val firstHeight = part1(input, (mu + lam)*rs, true)
    val lamHeight = firstHeight - muHeight
    val allCycles = 1000000000000L
    val repeat = (allCycles - mu*rs) / (lam*rs)
    val remainder = (allCycles - mu*rs) % (lam*rs)
    val remainderHeight = part1(input, mu*rs + remainder.toInt(), false)
    // println("$allCycles $repeat $remainder $lamHeight $remainderHeight")
    return repeat*lamHeight + remainderHeight
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("17_test")
  check(part1(testInput, 2022, false) == 3068)
  check(part2(testInput) == 1514285714288L)

  val input = readInput("17")
  println(part1(input, 2022, false))
  println(part2(input))
}
