package wittie.uius

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class ShapeHelper(val shapeRenderer: ShapeRenderer) {
    fun drawFilledRect(color: Color, pos: Position2i) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.rect(
            pos.xMin().toFloat(), pos.yMin().toFloat(), pos.dimensions.width.toFloat(),
            pos.dimensions.height.toFloat()
        )
        shapeRenderer.end()
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
data class Fill(val weight: Int = 100) : FillBehavior()
data class Percentage(val percentage: Int) : FillBehavior()

class Text(val text: String) : UiDrawable() {
//    private val font: BitmapFont = BitmapFont()

    init {
//        font.color = Color.RED
    }

    override fun children(position: Position2i): List<PositionedDrawable> = listOf()

//    override fun children(position: Position2i): List<PositionedDrawable> =
//
//    override fun children(position: Position2i): List<PositionedDrawable> {
//        return listOf(PositionedDrawable(this, position))
//    }

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {
        shapeHelper.drawFilledRect(Color.BLUE, position)

//        shapeHelper.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
//        shapeHelper.shapeRenderer.color = Color.BLUE
//        shapeHelper.shapeRenderer.rect(
//            position.xMin().toFloat(), position.yMin().toFloat(), position.dimensions.width.toFloat(),
//            position.dimensions.height.toFloat()
//        )
//        shapeHelper.shapeRenderer.end()
    }

    override fun type(): String {
        return "Text"
    }
}

class Rectangle(private val fill: Color) : UiDrawable() {
    private val shapeRenderer = ShapeRenderer()

    override fun children(position: Position2i): List<PositionedDrawable> = listOf()

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {
        shapeHelper.drawFilledRect(Color.BLUE, position)

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
//        shapeRenderer.color = fill
//        shapeHelper.shapeRenderer.rect(
//            position.xMin().toFloat(), position.yMin().toFloat(), position.dimensions.width.toFloat(),
//            position.dimensions.height.toFloat()
//        )
    }

    override fun type(): String {
        return "Rectangle"
    }
}

sealed class Layout : UiDrawable()

class Single(private val element: UiDrawable) : Layout() {
    override fun children(position: Position2i): List<PositionedDrawable> =
        listOf(PositionedDrawable(element, position))

//    override fun children(position: Position2i): List<PositionedDrawable> = listOf()
//        return listOf(PositionedDrawable(this, position))
//    }

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {}

    override fun type(): String {
        return "Single"
    }
}

data class UiDrawableWithFill(val drawable: UiDrawable, val fill: FillBehavior)

object LayoutFns {
    fun fixedSum(elements: List<UiDrawableWithFill>): Int =
        elements.flatMap { element ->
            when (element.fill) {
                is Fill -> listOf()
                is Fixed -> listOf(element.fill.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

    fun percentageSum(elements: List<UiDrawableWithFill>): Int =
        elements.map { element ->
            when (element.fill) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.fill.percentage
            }
        }.fold(0) { a, b -> a + b }

    fun fillWeightSum(elements: List<UiDrawableWithFill>): Int =
        elements.map { element ->
            when (element.fill) {
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
        if (elements.isEmpty())
            return listOf()

        val fixedTotal = elements.flatMap { element ->
            when (element.fill) {
                is Fill -> listOf()
                is Fixed -> listOf(element.fill.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

        val percentageTotal = elements.map { element ->
            when (element.fill) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.fill.percentage
            }
        }.fold(0) { a, b -> a + b } * (position.dimensions.height - fixedTotal)

        val fillTotal = elements.map { element ->
            when (element.fill) {
                is Fill -> element.fill.weight
                is Fixed -> 0
                is Percentage -> 0
            }
        }.fold(0) { a, b -> a + b }

        return elements.fold<UiDrawableWithFill, Pair<List<PositionedDrawable>, Int>>(
            Pair(
                listOf(),
                position.lowerLeft.x
            )
        ) { (elementsSoFar, xAt), element ->
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

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {}

    override fun type(): String {
        return "Horizontal"
    }
}

fun toLayout(background: Background): Layout {
    return Single(background.toRectangle())
}

class RectangularArea(val layouts: List<Layout> = listOf()) : UiDrawable() {
    constructor(layout: Layout) : this(listOf(layout))
    constructor(background: Background, layout: Layout) : this(listOf(layout, toLayout(background)))
    constructor(background: Background, layouts: List<Layout> = listOf()) : this(listOf(toLayout(background)) + layouts)

    override fun children(position: Position2i): List<PositionedDrawable> {
        return layouts.flatMap { layout -> layout.children(position) }
    }

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {}

    override fun type(): String {
        return "RectangularArea"
    }
}

//fun go(batch: Batch) {
//    val ui = ui()
////    ui.draw(batch)
//}
//
//
//class Ui2 {
//}