package wittie.uius.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wittie.uius.Point2i
import wittie.uius.Position2i
import wittie.uius.ShapeHelper
import wittie.uius.ui.elements.drawables.Rectangle

interface Background {
    fun toRectangle(): Rectangle
}

class ColorFill(val color: Color) : Background {
    override fun toRectangle(): Rectangle {
        return Rectangle(color)
    }
}

abstract class Layout : UiContainer()

//fun toLayout(background: Background): Layout {
//    return Rectangle(background.toRectangle())
//}



data class PositionedContainer(
    val container: UiContainer, val position: Position2i,
    val childContainers: List<PositionedContainer> = listOf(),
    val childDrawables: List<Pair<UiDrawable, Position2i>> = listOf(),
    val descendantDrawables: List<Pair<UiDrawable, Position2i>> = listOf()
) {
    fun addDrawable(child: UiDrawable, position: Position2i): PositionedContainer =
        this.copy(childDrawables = this.childDrawables + Pair(child, position), descendantDrawables = this.descendantDrawables + Pair(child, position))

    fun addContainer(positioned: PositionedContainer): PositionedContainer =
        this.copy(childContainers = this.childContainers + positioned, descendantDrawables = this.descendantDrawables + positioned.descendantDrawables)

    fun detectHit(point: Point2i): Set<PositionedContainer> {
        // Assumes children fit within its boundaries. Rendering does not enforce this.
        if (!position.contains(point)) {
            return setOf()
        }
        return setOf(this) + childContainers.flatMap { c -> c.detectHit(point) }
    }
}


sealed interface UiElement {
    fun type(): String
}

abstract class UiDrawable : UiElement {
    abstract fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i)
}

abstract class UiContainer : UiElement {
    abstract fun positioned(position: Position2i): PositionedContainer
}