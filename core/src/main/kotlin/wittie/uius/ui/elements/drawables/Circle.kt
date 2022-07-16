package wittie.uius.ui.elements.drawables

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wittie.uius.Point2i
import wittie.uius.Position2i
import wittie.uius.ShapeHelper
import wittie.uius.ui.UiDrawable
import wittie.uius.ui.elements.containers.RectangularArea

class Circle(private val fillColor: Color) : UiDrawable() {

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {
        shapeHelper.drawFilledCircle(batch, fillColor, position.center(), position.minDim())
    }

    override fun type(): String {
        return "Circle"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Circle

        if (fillColor != other.fillColor) return false

        return true
    }

    override fun hashCode(): Int {
        return fillColor.hashCode()
    }
}

fun positionedCircle(center: Point2i, radius: Int, fillColor: Color): RectangularArea =
    RectangularArea()