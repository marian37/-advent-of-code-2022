import java.io.File
import java.util.PriorityQueue
import kotlin.math.ceil

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  data class Blueprint(val id: Int, val o: Int, val c: Int, val ob: Pair<Int, Int>, val g: Pair<Int, Int>)

  data class State(val robots: List<Int>, val materials: List<Int>)

  data class StateWithDist(val state: State, val d: Int): Comparable<StateWithDist> {
    override fun compareTo(other: StateWithDist): Int {
      return d.compareTo(other.d)
    }
  }

  fun analyzeBlueprint(blueprint: Blueprint, initialState: State, totalTime: Int): Int {
    val dist = mutableMapOf(initialState to 0)
    val queue = PriorityQueue<StateWithDist>()
    queue.add(StateWithDist(initialState, 0))
    val visited = mutableSetOf<String>()
    val robots = mapOf(
      0 to listOf(blueprint.o, 0, 0, 0),
      1 to listOf(blueprint.c, 0, 0, 0),
      2 to listOf(blueprint.ob.first, blueprint.ob.second, 0, 0),
      3 to listOf(blueprint.g.first, 0, blueprint.g.second, 0),
    )
    val maxRobots = listOf(
      maxOf(blueprint.o, blueprint.c, blueprint.ob.first, blueprint.g.first),
      blueprint.ob.second,
      blueprint.g.second,
    )
    // var c = 0
    while (!queue.isEmpty()) {
      val us = queue.poll()
      val u = us.state
      val distU = us.d
      visited.add(u.robots.joinToString("|"))

      for ((robot, cost) in robots) {
        if (robot != 3 && u.robots[robot] >= maxRobots[robot]) continue
        val d = cost.mapIndexed { i, c -> if (c - u.materials[i] <= 0) 1 else if (u.robots[i] != 0) ceil((c - u.materials[i]) / (u.robots[i] + 0.0)).toInt() + 1 else Int.MAX_VALUE }.max()
        if (d != Int.MAX_VALUE) {
          val v = State(
            u.robots.mapIndexed { i, r -> if (i == robot) r + 1 else r }, 
            u.materials.mapIndexed { i, m -> m + u.robots[i] * d - cost[i] }
          )
          // println("U: $u\nV: $v\nC: $cost\nD: $d")
          if (v.robots.joinToString("|") in visited) continue
          val distV = dist[v] ?: Int.MAX_VALUE
          if (distV > distU + d && distU + d <= totalTime) {
            dist[v] = distU + d
            queue.add(StateWithDist(v, distU + d))
          }
        }
      }

      // c++ 
      // if (c % 1000 == 0) println("$c ${queue.size}")
      // if (c > 100) {
      //   println(queue)
      //   println(dist)
      //   break
      // }
    }
    // println(dist)
    var maxG = 0
    for ((k, v) in dist) {
      val g = k.materials[3] + (totalTime - v) * k.robots[3]
      if (g > maxG) {
        maxG = g
        // println("M: $maxG $k $v ${dist[k]}")
      }
    }
    return maxG
  }

  fun solution(input: List<String>, remainingTime: Int, part2: Boolean = false): Int {
    val regex = Regex("""Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")
    val blueprints = if (!part2) input else input.take(3)
    val geodes = blueprints.map { line ->
      regex.matchEntire(line)!!.destructured.let { (b, o, c, oo, oc, go, gob) ->
        val blueprint = Blueprint(b.toInt(), o.toInt(), c.toInt(), Pair(oo.toInt(), oc.toInt()), Pair(go.toInt(), gob.toInt()))
        analyzeBlueprint(blueprint, State(listOf(1, 0, 0, 0), listOf(0, 0, 0, 0)), remainingTime).also{println(it)}
      }
    }
    return if (!part2) geodes.mapIndexed { i, g -> g*(i+1) }.sum()
    else geodes.reduce { acc, count -> acc * count }
  }

  // test if implementation meets criteria from the description, like:
  val REMAINING_TIME = 24
  val REMAINING_TIME2 = 32
  val testInput = readInput("19_test")
  check(solution(testInput, REMAINING_TIME) == 33)
  check(solution(testInput, REMAINING_TIME2, true) == 62*56)

  val input = readInput("19")
  println(solution(input, REMAINING_TIME))
  println(solution(input, REMAINING_TIME2, true))
}
