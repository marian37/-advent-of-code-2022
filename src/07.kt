import java.io.File

sealed class AoCFile(val isDirectory: Boolean, open val name: String, open val parent: AoCFile?) {
  abstract fun getSize(): Long
}

class Dir(override val name: String, override val parent: AoCFile?, val children: MutableList<AoCFile>): AoCFile(true, name, parent) {
  override fun getSize(): Long = children.sumOf{ f -> f.getSize()}
}

class DiskFile(override val name: String, override val parent: AoCFile, val fileSize: Long): AoCFile(false, name, parent) {
  override fun getSize(): Long = fileSize
}

fun main() {
  fun readInput(name: String) = File("$name.txt").readLines()

  fun parseInput(input: List<String>): Dir {
    val root = Dir("/", null, mutableListOf())
    var current: Dir = root
    for (line in input) {
      val lineParts = line.split(" ")      
      when (lineParts[0]) {
        "$" -> {
          if (lineParts[1] == "cd" && lineParts[2] != "/") {
            if (lineParts[2] == "..") {
              current = current.parent as Dir
            } else {
              val dir = current.children.find{ it.name == lineParts[2] }
              if (dir != null && dir is Dir) current = dir
            }
          }
        }
        "dir" -> {
          val dir = Dir(lineParts[1], current, mutableListOf())
          current.children.add(dir)
        }
        else -> {
          val file = DiskFile(lineParts[1], current, lineParts[0].toLong())
          current.children.add(file)
        }
      }      
    }
    return root
  }

  fun countSmaller(current: Dir, atMost: Long): Long {    
    val listOfDirs = mutableListOf<Dir>()
    var count = 0L
    for (c in current.children) {
      if (c is Dir) {
        val size = c.getSize()
        if (size <= atMost) {
          count += size
        }
        listOfDirs.add(c)
      }
    }    
    for (d in listOfDirs) {
      count += countSmaller(d, atMost)
    }
    return count
  }

  fun smallestEnough(current: Dir, minimum: Long): Long {    
    val listOfDirs = mutableListOf<Dir>()
    var currMin = Long.MAX_VALUE
    for (c in current.children) {
      if (c is Dir) {
        val size = c.getSize()
        if (size < currMin && size >= minimum) {
          currMin = size
        }
        listOfDirs.add(c)
      }
    }
    for (d in listOfDirs) {
      val s = smallestEnough(d, minimum)
      if (s < currMin && s >= minimum) currMin = s
    }
    return currMin
  }

  fun part1(input: List<String>): Long {
    val root = parseInput(input)
    return countSmaller(root, 100000)
  }

  fun part2(input: List<String>): Long {
    val root = parseInput(input)    
    val s = smallestEnough(root, root.getSize() + 30000000L - 70000000L)
    return s
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("07_test")
  check(part1(testInput) == 95437L)
  check(part2(testInput) == 24933642L)

  val input = readInput("07")
  println(part1(input))
  println(part2(input))
}