import java.io.File

data class Node(val value: Long, var prev: Node?, var next: Node?) {
  fun move(i: Int) {
    if (i == 0) return
    val n = next!!
    val p = prev!!
    p.next = n
    n.prev = p
    var c = this
    when {
      i > 0 -> {
        repeat(i) {
          c = c.next!!
        }
      }
      i < 0 -> {
        repeat(1-i) {
          c = c.prev!!
        }
      }
      else -> {
        return
      }
    }
    this.prev = c
    this.next = c.next
    this.prev?.next = this
    this.next?.prev = this
  }

  fun jump(i: Int): Node {
    var c = this
    if (i > 0) {
      repeat(i) {
        c = c.next!!
      }
    } else {
      repeat(1-i) {
        c = c.prev!!
      }
    }
    return c
  }

  override fun toString(): String {
    return "${this.value} (${this.prev?.value}, ${this.next?.value})"
  }
}

data class LinkedList(val list: List<Long>) {
  var current: Node? = null
  val mapping: MutableMap<Int, Node> = mutableMapOf()

  init {
    var curr: Node? = null
    var prev: Node? = null
    for (l in list.indices) {
      curr = Node(list[l], prev, null)
      if (l == 0) current = curr
      prev?.next = curr
      mapping.put(l, curr)
      prev = curr
    }
    curr?.next = current
    current?.prev = curr
    current = mapping[list.indexOf(0)]
  }

  fun find(index: Int): Node {
    return mapping[index]!!
  }

  override fun toString(): String {
    var c = current?.next
    val s = StringBuilder("${current?.value}, ")
    var count = 1
    while (c?.value != current?.value) {
      s.append("${c?.value}, ")
      c = c?.next
      count++
    }
    s.append("\n$count")
    return s.toString()
  }
}

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines().map { it.toInt() }

  fun mix(input: List<Long>, times: Int): LinkedList {
    val seq = LinkedList(input)
    repeat(times) {
      for (i in input.indices) {
        val j = seq.find(i)
        j.move((input[i] % (input.size-1)).toInt())
      }
    }
    return seq
  }

  fun solution(input: List<Int>, part2: Boolean): Long {
    val repeatCount = if (part2) 10 else 1
    val decryptionKey = if (part2) 811589153L else 1L
    val mappedInput = input.map { it * decryptionKey }
    val mixed = mix(mappedInput, repeatCount)
    var pos = mixed.find(input.indexOf(0))
    var sum = 0L
    repeat(3) {
      pos = pos.jump(1000)
      sum += pos.value
    }
    return sum
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("20_test")
  check(solution(testInput, false) == 3L)
  check(solution(testInput, true) == 1623178306L)

  val input = readInput("20")
  println(solution(input, false))
  println(solution(input, true))
}
