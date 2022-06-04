package wittie.uius.ui

import com.badlogic.gdx.graphics.Color
import wittie.uius.Position2i

data class PositionedContainer(val uiContainer: UiContainer, val position: Position2i)

data class PositionedDrawable(val uiDrawable: UiDrawable, val position: Position2i)

interface Background {
    fun toRectangle(): Rectangle
}

class ColorFill(val color: Color) : Background {
    override fun toRectangle(): Rectangle {
        return Rectangle(color)
    }
}

sealed class Layout : UiContainer()

data class UiDrawableWithFill(val drawable: UiContainer, val fill: FillBehavior)

