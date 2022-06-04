package wittie.uius.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wittie.uius.Position2i
import wittie.uius.ShapeHelper

class Text(val text: String) : UiDrawable() {
//    private val font: BitmapFont = BitmapFont()

    init {
//        font.color = Color.RED
    }

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