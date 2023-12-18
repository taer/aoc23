package point

data class Point2d(val x: Int, val y: Int) {

    operator fun plus(other: Point2d) = Point2d(x + other.x, y + other.y)

    companion object {
        val N = Point2d(0, -1)
        val S = Point2d(0, 1)
        val E = Point2d(1, 0)
        val W = Point2d(-1, 0)
        val cardinals = listOf(N,S,E,W)
    }
}