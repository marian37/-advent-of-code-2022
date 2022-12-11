import java.io.File
import java.math.BigInteger

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  var commonMod = 0

  data class Item(val item: Long) {
    fun isDivisible(factor: Int): Boolean {      
      return item.mod(factor.toLong()) == 0L
    }

    fun squared(): Item {
      return Item(item.times(item) % commonMod)
    }

    fun multipliedBy(factor: Int): Item {
      return Item(item.times(factor.toLong()) % commonMod)
    }

    fun added(factor: Int): Item {
      return Item(item.plus(factor.toLong()) % commonMod)
    }

    fun dividedBy(factor: Int): Item {
      return Item(item.div(factor.toLong()) % commonMod)
    }
  }

  data class Monkey(
    val id: Int, 
    val startingItems: List<Int>, 
    val operation: String,
    val operationFactor: Int?,
    val divTest: Int, 
    val divTestTrue: Int,
    val divTestFalse: Int,
    val items: ArrayDeque<Item>,
    var inspected: Long
  ) {
    fun provideOperation(item: Item): Item {
      if (operation == "*")
        return if (operationFactor == null) item.squared() else item.multipliedBy(operationFactor)
      return item.added(operationFactor ?: 0)
    }

    fun provideTest(item: Item): Int {
      inspected++
      return if (item.isDivisible(divTest)) divTestTrue else divTestFalse
    }
  }

  fun parseMonkeys(input: List<String>): List<Monkey> {
    val monkeys = mutableListOf<Monkey>()
    for (monkeyDesc in input.windowed(6, 7)) {
      val id = "${monkeyDesc[0][7]}".toInt()
      val startingItems = monkeyDesc[1].substringAfter(':').trim().split(" ").map { it.substringBefore(',').toInt() }
      val operation = monkeyDesc[2].substringAfter(':').trim().split(" ")
      val operationFactor = if (operation.last() == "old") null else operation.last().toInt()
      val divTest = monkeyDesc[3].split(" ").last().toInt()
      val divTestTrue = monkeyDesc[4].split(" ").last().toInt()
      val divTestFalse = monkeyDesc[5].split(" ").last().toInt()
      monkeys.add(Monkey(id, startingItems, operation[operation.size-2], operationFactor, divTest, divTestTrue, divTestFalse, ArrayDeque(startingItems.map{Item(it.toLong())}), 0L))
    }
    commonMod = monkeys.map{it.divTest}.reduce{acc, i -> acc * i}
    return monkeys
  }

  fun solution(input: List<String>, rounds: Int, divideBy3: Boolean): Long {
    val monkeys = parseMonkeys(input)
    repeat(rounds) {      
      // round
      for (monkey in monkeys) {
        while (!monkey.items.isEmpty()) {
          val item = monkey.items.removeFirst()          
          val operated = monkey.provideOperation(item)
          val afterOperation = if (divideBy3) operated.dividedBy(3) else operated
          val testResult = monkey.provideTest(afterOperation)          
          monkeys[testResult].items.addLast(afterOperation)
        }
      }
    }
    return monkeys.map{it.inspected}.sortedDescending().take(2).reduce{acc, i -> acc * i}
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("11_test")
  check(solution(testInput, 20, true) == 10605L)  
  check(solution(testInput, 10000, false) == 2713310158L)

  val input = readInput("11")
  println(solution(input, 20, true))
  println(solution(input, 10000, false))
}
