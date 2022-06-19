package wittie.uius

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.assets.toInternalFile
import ktx.freetype.*


enum class TextSize {
    SMALL, MEDIUM, LARGE
}

class ShapeHelper(val shapeRenderer: ShapeRenderer) {
    var generator = FreeTypeFontGenerator("fonts/open-sans/OpenSans-Regular.ttf".toInternalFile())

    val smallFont = generator.generateFont {
        size = 12
    }
    val regularFont = generator.generateFont {
        size = 20
//        size = 20
        color = Color.BLACK
//        outline
    }
    val bigFont = generator.generateFont {
        size = 42
    }

    fun drawFilledRect(color: Color, pos: Position2i) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.rect(
            pos.xMin().toFloat(), pos.yMin().toFloat(), pos.dimensions.width.toFloat(),
            pos.dimensions.height.toFloat()
        )
        shapeRenderer.end()
    }

    fun drawText(batch: Batch, text: String, point: Point2i, textSize: TextSize = TextSize.MEDIUM) {
//        batch.setColor(1f, 0f, 0f, 1f)
        val font = when (textSize) {
            TextSize.SMALL -> smallFont
            TextSize.MEDIUM -> regularFont
            TextSize.LARGE -> bigFont
        }

        batch.begin()
        font.draw(batch, text, point.x.toFloat(), point.y.toFloat() + 10)
        batch.end()
    }

    fun drawFilledCircle(color: Color, center: Point2i, radius: Int) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.circle(center.x.toFloat(), center.y.toFloat(), radius.toFloat())
        shapeRenderer.end()
    }
}