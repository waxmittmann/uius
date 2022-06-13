package wittie.uius.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import wittie.uius.Position2i
import wittie.uius.ShapeHelper

class Rectangle(private val fillColor: Color) : UiDrawable() {

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {
        shapeHelper.drawFilledRect(fillColor, position)

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rectangle

        if (fillColor != other.fillColor) return false

        return true
    }

    override fun hashCode(): Int {
        return fillColor.hashCode()
    }
}