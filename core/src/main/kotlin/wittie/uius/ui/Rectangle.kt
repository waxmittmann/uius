package wittie.uius.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import wittie.uius.Position2i
import wittie.uius.ShapeHelper

class Rectangle(private val fill: Color) : UiContainer() {
    private val shapeRenderer = ShapeRenderer()

    override fun childContainers(position: Position2i): List<PositionedContainer> = listOf()

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