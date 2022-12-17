import java.io.File
import java.util.PriorityQueue

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): Pair<Map<String, Int>, Map<String, List<String>>> {
    val regex = Regex("""Valve ([A-Z]+) has flow rate=(\d+); (\D+)""")
    val rates = mutableMapOf<String, Int>()
    val graph = mutableMapOf<String, List<String>>()
    input.forEach { line ->
      regex.matchEntire(line)!!.destructured.let { (valve, rate, secondPart) ->
        rates.put(valve, rate.toInt())
        graph.put(valve, secondPart.trim().split(" ").drop(4).map{ it.trim(',') })
      }
    }
    return Pair(rates, graph)
  }

  data class State(val remainingTime: Int, val current: String, val opened: Set<String> = emptySet())

  val cache1 = mutableMapOf<State, Int>()
  var graph1: Map<String, List<Pair<String, Int>>> = emptyMap()
  var rates: Map<String, Int> = emptyMap()

  fun reduceGraph(graph: Map<String, List<String>>, initialCurrent: String): Pair<Map<String, List<Pair<String, Int>>>, List<Pair<String, Int>>> {
    // rates
    val g = graph.mapValues { (_, v) -> v.map { Pair(it, 1) } }.toMutableMap()
    val zeroValves = rates.filter { (k, v) -> v == 0 && k != initialCurrent }.keys
    for (k in zeroValves) {
      val kNeighbors = g.remove(k)!!
      for ((n, d) in kNeighbors) {
        val original = g[n]!!
        g[n] = original.filter { it.first != k } + kNeighbors.filter { it.first != n }.map { Pair(it.first, it.second + d) }
      }
    }
    val k = initialCurrent
    val kNeighbors = g.remove(k)!!
    for ((n, d) in kNeighbors) {
      val original = g[n]!!
      g[n] = original.filter { it.first != k } + kNeighbors.filter { it.first != n }.map { Pair(it.first, it.second + d) }
    }
    return Pair(g, kNeighbors)
  }

  fun solve1(state: State): Int {
    val cached = cache1[state]
    if (cached != null) return cached
    if (state.remainingTime <= 1) return 0
    if (state.opened.containsAll(rates.filterValues { it != 0 }.map { it.key })) return 0
    
    var options = mutableListOf<Int>()
    if (state.current !in state.opened) {
      options.add((state.remainingTime-1)*rates[state.current]!! + solve1(State(state.remainingTime - 1, state.current, state.opened + setOf(state.current))))
    }
    val neighbors = graph1[state.current]!!
    for ((n, d) in neighbors) {
      options.add(solve1(State(state.remainingTime - d, n, state.opened)))
    }
    val res = options.max()
    cache1[state] = res
    return res
  }

  fun part1(input: List<String>, initialState: State): Int {
    val (r, g) = parseInput(input)
    rates = r
    cache1.clear()
    val (g1, currNeighbors) = reduceGraph(g, initialState.current)
    graph1 = g1
    val options = mutableListOf<Int>()
    for ((n, d) in currNeighbors) {
      options.add(solve1(State(initialState.remainingTime - d, n)))
    }
    return options.max()
  }

  fun floydWarshall(shortestPaths: MutableMap<String, MutableMap<String, Int>>): MutableMap<String, MutableMap<String, Int>> {
    for (k in shortestPaths.keys) {
      for (i in shortestPaths.keys) {
        for (j in shortestPaths.keys) {
          val ik = shortestPaths[i]?.get(k) ?: 9999
          val kj = shortestPaths[k]?.get(j) ?: 9999
          val ij = shortestPaths[i]?.get(j) ?: 9999
          if (ik + kj < ij)
              shortestPaths[i]?.set(j, ik + kj)
        }
      }
    }
    //remove all paths that lead to a valve with rate 0
    shortestPaths.values.forEach {
      it.keys.map { key -> if (rates[key]!! == 0) key else "" }
        .forEach { toRemove -> if (toRemove != "") it.remove(toRemove) }
    }
    return shortestPaths
  }

  var shortestPaths: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()
  var score = 0
  var totalTime = 30
  
  fun dfs(currScore: Int, currentValve: String, visited: Set<String>, time: Int, part2: Boolean = false) {
    score = maxOf(score, currScore)
    for ((valve, dist) in shortestPaths[currentValve]!!) {
      if (!visited.contains(valve) && time + dist + 1 < totalTime) {
        dfs(
          currScore + (totalTime - time - dist - 1) * rates[valve]!!,
          valve,
          visited.union(listOf(valve)),
          time + dist + 1,
          part2
        )
      }
    }
    if (part2)
      dfs(currScore, "AA", visited, 0, false)
  }

  // Inspired by https://github.com/ckainz11/AdventOfCode2022/blob/main/src/main/kotlin/days/day16/Day16.kt
  fun part2(input: List<String>, initialState: State): Int {
    val (r, g) = parseInput(input)
    rates = r
    shortestPaths = floydWarshall(g.mapValues { (_, v) -> v.associateWith { 1 }.toMutableMap() }.toMutableMap())
    totalTime = initialState.remainingTime
    dfs(0, initialState.current, emptySet(), 0, true)
    return score
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("16_test")
  check(part1(testInput, State(30, "AA")) == 1651)
  check(part2(testInput, State(26, "AA")) == 1707)

  val input = readInput("16")
  println(part1(input, State(30, "AA")))
  println(part2(input, State(26, "AA")))
}
