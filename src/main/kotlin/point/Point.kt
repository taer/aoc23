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