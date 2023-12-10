package point

data class Point(val x: Int, val y: Int)

fun Point.neighbors() =
    setOf(
        copy(x = x + 1),
        copy(x = x - 1),
        copy(y = y + 1),
        copy(y = y - 1),
        copy(x = x - 1, y = y - 1),
        copy(x = x + 1, y = y + 1),
        copy(x = x + 1, y = y - 1),
        copy(x = x - 1, y = y + 1),
    )
fun Point.cardinals() =
    setOf(
        copy(x = x + 1),
        copy(x = x - 1),
        copy(y = y + 1),
        copy(y = y - 1),
    )

enum class Direction{
    N,S,E,W
}
fun Point.cardinalsWithDir() =
    listOf(
        Direction.S to copy(x = x + 1),
        Direction.N to copy(x = x - 1),
        Direction.E to copy(y = y + 1),
        Direction.W to copy(y = y - 1),
    )

