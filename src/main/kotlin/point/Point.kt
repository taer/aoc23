package point

data class Point(val x: Int, val y: Int) {
}

fun Point.dirTo(point: Point): Direction {
    val rowTheSame = this.x == point.x
    val yTheSame = y == point.y

    require(rowTheSame || yTheSame ){
        "points must be in a line. $this, $point"
    }
    require(this != point){
        "Same points"
    }
    return if(rowTheSame){
        if(y < point.y){
            Direction.W
        }else{
            Direction.E
        }
    }else{
        if(x < point.x){
            Direction.N
        }else{
            Direction.S
        }

    }
}

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
fun Point.move(dir: Direction)= when(dir){
    Direction.N -> copy(x=x-1)
    Direction.S -> copy(x=x+1)
    Direction.E -> copy(y=y+1)
    Direction.W -> copy(y=y-1)
}
fun Point.cardinals() =
    setOf(
        copy(x = x + 1),
        copy(x = x - 1),
        copy(y = y + 1),
        copy(y = y - 1),
    )

enum class Direction{
    N,S,E,W;
    fun reversed() = when(this){
        N -> S
        S -> N
        E -> W
        W -> E
    }
}
fun Point.cardinalsWithDir() =
    listOf(
        Direction.S to copy(x = x + 1),
        Direction.N to copy(x = x - 1),
        Direction.E to copy(y = y + 1),
        Direction.W to copy(y = y - 1),
    )

