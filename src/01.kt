import java.io.File

fun main() {
    fun readInput(name: String) = File("$name.txt").readLines()

    fun part1(input: List<String>): Int {
        var max = -1
        var tempSum = 0
        for (line in input) {
            if (line == "") {
                max = if (tempSum > max) tempSum else max
                tempSum = 0
            } else {
                tempSum += line.toInt()
            }
        }
        max = if (tempSum > max) tempSum else max
        return max
    }

    fun part2(input: List<String>): Int {
        val sums = mutableListOf<Int>()
        var tempSum = 0
        for (line in input) {
            if (line == "") {
                sums.add(tempSum)
                tempSum = 0
            } else {
                tempSum += line.toInt()
            }
        }
        sums.add(tempSum)
        sums.sortDescending()
        return sums.slice(0..2).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("01")
    println(part1(input))
    println(part2(input))
}
