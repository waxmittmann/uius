package wittie.uius

data class Point2i(val x: Int, val y: Int) {
    fun set(newX: Int = x, newY: Int = y): Point2i {
        return Point2i(newX, newY)
    }
    override fun toString(): String = "(x: $x, y: $y)"
}

data class Rect2i(val ll: Point2i, val ur: Point2i)
data class Dimensions2i(val width: Int, val height: Int) {
    fun modify(widthFn: (Int) -> Int = { i -> i }, heightFn: (Int) -> Int = { i -> i }): Dimensions2i {
        return Dimensions2i(widthFn(width), heightFn(height))
    }

    fun set(newWidth: Int = width, newHeight: Int = height): Dimensions2i {
        return Dimensions2i(newWidth, newHeight)
    }

    override fun toString(): String = "(width: $width, height: $height)"
}

data class Position2i(val lowerLeft: Point2i, val dimensions: Dimensions2i) {
//    companion object {
//        fun from(ll: Int, ul: Int, ur: Int, lr: Int): Position2i = Position2i(Point2i(ll, ))
//    }

    constructor(lx: Int, ly: Int, width: Int, height: Int) : this(Point2i(lx, ly), Dimensions2i(width, height))

    override fun toString(): String = "(pos: $lowerLeft, dims: $dimensions)"
    fun yMax(): Int {
        return lowerLeft.y + dimensions.height
    }

    fun xMin(): Int {
        return lowerLeft.x
    }

    fun yMin(): Int {
        return lowerLeft.y
    }

    fun toRect(): Rect2i = Rect2i(lowerLeft, lowerLeft.let { ll -> Point2i(ll.x + dimensions.width, ll.y + dimensions.height) })
}