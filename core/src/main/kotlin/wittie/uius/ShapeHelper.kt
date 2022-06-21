package wittie.uius

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.assets.toInternalFile
import ktx.freetype.*


enum class TextSize {
    SMALL, MEDIUM, LARGE
}

data class UiusFont(val size: Int, val font: BitmapFont)

class ShapeHelper(val shapeRenderer: ShapeRenderer) {
    var generator = FreeTypeFontGenerator("fonts/open-sans/OpenSans-Regular.ttf".toInternalFile())

    val smallFont = run {
        val fontSize = 10
        UiusFont(
            size = fontSize,
            font = generator.generateFont {
                size = fontSize
            }
        )
    }

    val regularFont = run {
        val fontSize = 20
        UiusFont(
            size = fontSize,
            font = generator.generateFont {
                size = fontSize
                color = Color.BLACK
            }
        )
    }

    val bigFont = run {
        val fontSize = 40
        UiusFont(
            size = fontSize,
            font = generator.generateFont {
                size = fontSize
                color = Color.BLACK
            }
        )
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
        val fontData = when (textSize) {
            TextSize.SMALL -> smallFont
            TextSize.MEDIUM -> regularFont
            TextSize.LARGE -> bigFont
        }

        batch.begin()
        fontData.font.draw(batch, text, point.x.toFloat(), point.y.toFloat() + fontData.size)
        batch.end()
    }

    fun drawFilledCircle(color: Color, center: Point2i, radius: Int) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.circle(center.x.toFloat(), center.y.toFloat(), radius.toFloat())
        shapeRenderer.end()
    }
}