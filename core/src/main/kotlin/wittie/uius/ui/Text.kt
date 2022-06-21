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

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {

        shapeHelper.drawText(batch, text, position.lowerLeft)
//        shapeHelper.drawFilledRect(Color.BLUE, position)
    }

    override fun type(): String {
        return "Text"
    }
}