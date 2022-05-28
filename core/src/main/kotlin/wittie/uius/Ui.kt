package wittie.uius

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/*


fun Menu(): Rectangle {
    Rectangle(
        background = DARK_GRAY,
        Vertical(
            Add(
                content = Rectangle(
                    background = GRAY,
                    Single(Text("Game"))
                ),
                x = Percentage(20),
                y = FILL,
            ),
            Add(
                content = Rectangle(
                    background = LIGHT_GRAY,
                    Single(Text("Options"))
                ),
                x = Percentage(20),
                y = FILL,
            ),
        )
    )
}

fun GameArea(): Rectangle = Rectangle.drawMethod(mainLoop)

ui = Horizontal(
        Add(content = Menu(), x = Fixed(120, PX), y = FILL),
        Add(content = GameArea(), x = FILL, y = FILL),
    )
ui.draw(batch)


 */

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
    override fun toString(): String = "(pos: $lowerLeft, dims: $dimensions)"
    fun yMax(): Int {
        return lowerLeft.y + dimensions.height
    }
}

data class PositionedDrawable(val uiDrawable: UiDrawable, val position: Position2i)

//sealed class Element : UiDrawable()

interface Background {
    fun toRectangle(): Rectangle
}

class ColorFill(val color: Color) : Background {
    override fun toRectangle(): Rectangle {
        return Rectangle(color)
    }
}

sealed class FillBehavior
data class Fixed(val pixels: Int) : FillBehavior()
data class Fill(val weight: Int = 100): FillBehavior()
data class Percentage(val percentage: Int): FillBehavior()

class Text(val text: String) : UiDrawable() {
//    private val font: BitmapFont = BitmapFont()

    init {
//        font.color = Color.RED
    }

    override fun draw(batch: Batch, dims: Dimensions2i) {
//        font.draw(batch, "Hello World", dims.width / 2f, dims.height / 2f)
    }

    override fun children(position: Position2i): List<PositionedDrawable> {
        return listOf(PositionedDrawable( this, position))
    }
}

class Rectangle(private val fill: Color) : UiDrawable() {
    private val shapeRenderer = ShapeRenderer()

    override fun draw(batch: Batch, dims: Dimensions2i) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = fill
        shapeRenderer.rect(0f, 0f, dims.width.toFloat(), dims.height.toFloat())
    }

    override fun children(position: Position2i): List<PositionedDrawable> {
        return listOf(PositionedDrawable(this, position))
    }
}

sealed class Layout : UiDrawable()

class Single(private val element: UiDrawable) : Layout() {
    override fun draw(batch: Batch, dims: Dimensions2i) {
        element.draw(batch, dims)
    }

    override fun children(position: Position2i): List<PositionedDrawable> {
        return listOf(PositionedDrawable(this, position))
    }
}

data class UiDrawableWithFill(val drawable: UiDrawable, val fill: FillBehavior)

object LayoutFns {
    fun fixedSum(elements: List<UiDrawableWithFill>): Int =
        elements.flatMap { element ->
            when(element.fill) {
                is Fill -> listOf()
                is Fixed -> listOf(element.fill.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

    fun percentageSum(elements: List<UiDrawableWithFill>): Int =
        elements.map { element ->
            when(element.fill) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.fill.percentage
            }
        }.fold(0){ a, b  -> a + b }

    fun fillWeightSum(elements: List<UiDrawableWithFill>): Int =
        elements.map { element ->
            when(element.fill) {
                is Fill -> element.fill.weight
                is Fixed -> 0
                is Percentage -> 0
            }
        }.fold(0) { a, b -> a + b }
}

class Horizontal : Layout() {
    //    private var elements: MutableList<Pair<Element, FillBehavior>> = mutableListOf()
    private var elements: MutableList<UiDrawableWithFill> = mutableListOf()

    fun add(content: UiDrawable, fill: FillBehavior) {
        elements.add(UiDrawableWithFill(content, fill))
    }

    override fun children(position: Position2i): List<PositionedDrawable> {
        if(elements.isEmpty())
            return listOf()

        val fixedTotal = elements.flatMap { element ->
            when(element.fill) {
                is Fill -> listOf()
                is Fixed -> listOf(element.fill.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

        val percentageTotal = elements.map { element ->
            when(element.fill) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.fill.percentage
            }
        }.fold(0){ a, b  -> a + b } * (position.dimensions.height - fixedTotal)

        val fillTotal = elements.map { element ->
            when(element.fill) {
                is Fill -> element.fill.weight
                is Fixed -> 0
                is Percentage -> 0
            }
        }.fold(0) { a, b -> a + b }

        return elements.fold<UiDrawableWithFill, Pair<List<PositionedDrawable>, Int>>(Pair(listOf(), position.lowerLeft.x)) { (elementsSoFar, xAt), element ->
            when (element.fill) {
                is Fill -> {
                    val resultElement = element.drawable.children(
                        Position2i(
                            position.lowerLeft.set(newX = xAt),
                            position.dimensions.modify(widthFn = { width -> xAt + element.fill.weight / fillTotal * (width - fixedTotal - percentageTotal) })
                        )
                    )
                    Pair(
                        elementsSoFar + resultElement,
                        if (resultElement.isEmpty()) xAt else xAt + resultElement.last().position.lowerLeft.x + resultElement.last().position.dimensions.width
                    )
                }

                is Fixed -> {
                    val resultElement = PositionedDrawable(
                        element.drawable,
                        Position2i(
                            position.lowerLeft.set(newX = xAt),
                            position.dimensions.set(newWidth = element.fill.pixels)
                        )
                    )
                    Pair(elementsSoFar + resultElement, xAt + element.fill.pixels)
                }

                is Percentage -> {
                    val eleWidth = element.fill.percentage * percentageTotal
                    val resultElement = PositionedDrawable(
                        element.drawable,
                        Position2i(
                            position.lowerLeft.set(newX = xAt),
                            position.dimensions.set(newWidth = eleWidth)
                        )
                    )
                    Pair(elementsSoFar + resultElement, xAt + eleWidth)
                }
            }
        }.first
    }

    override fun draw(batch: Batch, dims: Dimensions2i) {
        if(elements.isEmpty())
            return
        var total = 0
//        element.element.draw(batch, Dimensions2i(dims.width, element.fill.pixels))

        val fixedTotal = elements.flatMap { element: UiDrawableWithFill ->
            when(element.fill) {
                is Fill -> listOf()
                is Fixed -> listOf(element.fill.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

        val percentageTotal = elements.map { element ->
            when(element.fill) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.fill.percentage
            }
        }.fold(0) { a, b -> a + b } * (dims.height - fixedTotal)

        val fillTotal = elements.map { element ->
            when(element.fill) {
                is Fill -> element.fill.weight
                is Fixed -> 0
                is Percentage -> 0
            }
        }.fold(0) { a, b -> a + b }

        elements.forEach { element ->
            when(element.fill) {
                is Fill -> element.drawable.draw(batch, Dimensions2i( element.fill.weight / fillTotal * (dims.width - fixedTotal - percentageTotal), dims.height))
                is Fixed -> element.drawable.draw(batch, Dimensions2i( element.fill.pixels, dims.height))
                is Percentage -> element.drawable.draw(batch, Dimensions2i( element.fill.percentage * (dims.width - fixedTotal), dims.height))
            }
        }
    }
}

fun toLayout(background: Background): Layout {
    return Single(background.toRectangle())
}

class RectangularArea(val layouts: List<Layout> = listOf()) : UiDrawable() {
    constructor(layout: Layout): this(listOf(layout))
    constructor(background: Background, layout: Layout): this(listOf(layout, toLayout(background)))
    constructor(background: Background, layouts: List<Layout> = listOf()): this(listOf(toLayout(background)) + layouts)

    override fun draw(batch: Batch, dims: Dimensions2i) {
        layouts.forEach { l -> l.draw(batch, dims) }
    }

    override fun children(position: Position2i): List<PositionedDrawable> {
        return layouts.flatMap { layout -> layout.children(position) }
    }
}

fun menu(): RectangularArea {
    return RectangularArea(
        background = ColorFill(Color.DARK_GRAY),
        layout = Vertical().apply {
            add(
                content = RectangularArea(
                    background = ColorFill(Color.GRAY),
                    layout = Single(Text("Game"))
                ),
                fill = Percentage(20)
            )
            add(
                content = RectangularArea(
                    background = ColorFill(Color.LIGHT_GRAY),
                    layout = Single(Text("Options"))
                ),
                fill = Percentage(20)
            )
        }
    )
}

fun gameArea(): RectangularArea {
    return RectangularArea(
        background = ColorFill(Color.RED)
    )
}

fun ui(): RectangularArea {
    return RectangularArea(
        layout = Horizontal().apply {
            add(content = menu(), fill = Fixed(120))
            add(content = gameArea(), fill = Fill())
        }
    )
}

//fun go(batch: Batch) {
//    val ui = ui()
////    ui.draw(batch)
//}
//
//
//class Ui2 {
//}