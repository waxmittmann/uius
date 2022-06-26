package wittie.uius

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import ktx.assets.toInternalFile
import ktx.freetype.*


enum class TextSize {
    SMALL, MEDIUM, LARGE
}

data class UiusFont(val size: Int, val font: BitmapFont)

class ShapeHelper(val shapeRenderer: ShapeRenderer) {
//    var projectionMatrix: Matrix4 = Matrix4()
    var generator = FreeTypeFontGenerator("fonts/open-sans/OpenSans-Regular.ttf".toInternalFile())

    fun setProjectionMatrix(projectionMatrix: Matrix4) {
        shapeRenderer.projectionMatrix = projectionMatrix
    }

    private val smallFont =
            generator.generateFont {
                size = 10
            }

    private val regularFont =
            generator.generateFont {
                size = 20
                color = Color.BLACK
            }

    private val bigFont =
            generator.generateFont {
                size = 40
                color = Color.BLACK
            }

    fun drawFilledRect(batch: Batch, color: Color, pos: Position2i) {
//        batch.begin()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.rect(
            pos.xMin().toFloat(), pos.yMin().toFloat(), pos.dimensions.width.toFloat(),
            pos.dimensions.height.toFloat()
        )
        shapeRenderer.end()
//        batch.end()
    }

    fun drawText(batch: Batch, text: String, point: Point2i, textSize: TextSize = TextSize.MEDIUM) {
//        batch.setColor(1f, 0f, 0f, 1f)
        val fontData = when (textSize) {
            TextSize.SMALL -> smallFont
            TextSize.MEDIUM -> regularFont
            TextSize.LARGE -> bigFont
        }

        batch.begin()
        fontData.draw(batch, text, point.x.toFloat(), point.y.toFloat() + fontData.lineHeight)
        batch.end()
    }

    fun drawFilledCircle(batch: Batch, color: Color, center: Point2i, radius: Int) {
//        batch.begin()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = color
        shapeRenderer.circle(center.x.toFloat(), center.y.toFloat(), radius.toFloat())
        shapeRenderer.end()
//        batch.end()
    }
}