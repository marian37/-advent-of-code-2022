import java.io.File
import java.util.SortedSet
import kotlin.math.abs
import kotlin.math.max
  
typealias Sensor = Pair<Int, Int>
typealias Beacon = Pair<Int, Int>

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): List<Pair<Sensor, Beacon>> {
    val regex = Regex("""Sensor at x=(\d+), y=(\d+): closest beacon is at x=(-?\d+), y=(\d+)""")
    return input.map { line ->
      regex.matchEntire(line)!!.destructured.let { (x1, y1, x2, y2) ->
        Pair(Sensor(x1.toInt(), y1.toInt()), Beacon(x2.toInt(), y2.toInt()))
      }
    }
  }

  fun sweep(intervals: List<Pair<Int, Int>>, beaconsAtRow: SortedSet<Int>): Int {
    var start = intervals[0].first
    var end = intervals[0].second
    var count = 0
    for (interval in intervals.drop(1)) {
      if (interval.first > end) {
        count += end - start - beaconsAtRow.count { it >= start && it <= end }
        start = interval.first
        end = interval.second
      } else {
        end = max(end, interval.second)
      }
    }
    count += end - start + 1 - beaconsAtRow.count { it >= start && it <= end }
    return count
  }

  fun sweep2(intervals: List<Pair<Int, Int>>): Int? {
    var end = intervals[0].second
    for (interval in intervals.drop(1)) {
      if (interval.first > end) {
        return interval.first - 1
      } else {
        end = max(end, interval.second)
      }
    }
    return null
  }

  fun part1(input: List<String>, row: Int): Int {
    val parsedInput = parseInput(input)
    val intervals = parsedInput.map { (sensor, beacon) -> 
      val distance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
      val allowed = distance - abs(sensor.second - row)
      if (allowed >= 0) Pair(sensor.first - allowed, sensor.first + allowed) else null
    }
    val sortedIntervals = intervals.filterNotNull().sortedBy { it.first }
    val beaconsAtRow = parsedInput.filter { it.second.second == row }.map { it.second.first }.toSortedSet()
    return sweep(sortedIntervals, beaconsAtRow)
  }

  fun part2(input: List<String>, maxSize: Int): Long {
    val parsedInput = parseInput(input)
    var distressBeacon: Beacon? = null
    for (row in 0..maxSize) {
      val intervals = parsedInput.map { (sensor, beacon) -> 
        val distance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
        val allowed = distance - abs(sensor.second - row)
        if (allowed >= 0) Pair(max(sensor.first - allowed, 0), sensor.first + allowed) else null
      }
      val sortedIntervals = intervals.filterNotNull().sortedBy { it.first }
      val x = sweep2(sortedIntervals)
      if (x != null) {
        distressBeacon = Beacon(x, row)        
        break
      }
    }
    return if (distressBeacon != null) distressBeacon.first * 4000000L + distressBeacon.second else 0L
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("15_test")
  check(part1(testInput, 10) == 26)
  check(part2(testInput, 20) == 56000011L)

  val input = readInput("15")
  println(part1(input, 2000000))
  println(part2(input, 4000000))
}
