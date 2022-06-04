package wittie.uius

import com.badlogic.gdx.graphics.Color
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