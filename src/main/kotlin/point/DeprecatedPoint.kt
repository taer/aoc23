package point

data class DeprecatedPoint(val x: Int, val y: Int) {
}

fun DeprecatedPoint.dirTo(point: DeprecatedPoint): DeprecatedDirection {
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
            DeprecatedDirection.W
        }else{
            DeprecatedDirection.E
        }
    }else{
        if(x < point.x){
            DeprecatedDirection.N
        }else{
            DeprecatedDirection.S
        }

    }
}

fun DeprecatedPoint.neighbors() =
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
fun DeprecatedPoint.move(dir: DeprecatedDirection)= when(dir){
    DeprecatedDirection.N -> copy(x=x-1)
    DeprecatedDirection.S -> copy(x=x+1)
    DeprecatedDirection.E -> copy(y=y+1)
    DeprecatedDirection.W -> copy(y=y-1)
}
fun DeprecatedPoint.cardinals() =
    setOf(
        copy(x = x + 1),
        copy(x = x - 1),
        copy(y = y + 1),
        copy(y = y - 1),
    )

enum class DeprecatedDirection{
    N,S,E,W;
    fun reversed() = when(this){
        N -> S
        S -> N
        E -> W
        W -> E
    }
}
fun DeprecatedPoint.cardinalsWithDir() =
    listOf(
        DeprecatedDirection.S to copy(x = x + 1),
        DeprecatedDirection.N to copy(x = x - 1),
        DeprecatedDirection.E to copy(y = y + 1),
        DeprecatedDirection.W to copy(y = y - 1),
    )

