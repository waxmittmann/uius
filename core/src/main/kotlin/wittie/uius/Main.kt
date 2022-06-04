package wittie.uius

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import wittie.uius.ui.PositionedContainer

class Main : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}

class FirstScreen : KtxScreen {
    private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }
    private val batch = SpriteBatch()
    private var time: Float = 0f
    private var lastTime: Float = 0f

    private val ui = ui()


    init {
        fun localFn(positionedContainer: PositionedContainer): String {
            return positionedContainer.uiContainer.type() + ", " + positionedContainer.position.toString()
        }

        fun foldFn(lhs: String, rhs: String): String {
            return lhs + rhs
        }

        println("Result:\n" + ui().visit(Position2i(Point2i(0, 0), Dimensions2i(500, 500)), ::localFn, ::foldFn))
    }

    override fun render(delta: Float) {
        time += delta
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        val helper = ShapeHelper(ShapeRenderer())
        batch.use {
//            it.draw(image, 100f, 160f)

            if (true) {
//                val result = ui().visit(Position2i(0, 0, 500, 500), { pd ->
                val result = ui.visit(Position2i(0, 0, 300, 300), { pd ->
                    pd.uiContainer.drawContent(batch, helper, pd.position)
                    listOf("Drew ${pd.uiContainer.type()} at ${pd.position.toString()}")
//                    println(pd.uiDrawable)
                }, { l: List<String>, r: List<String> -> l + r })

//                if (time > lastTime + 1) {
//                    println(result)
//                    lastTime = time
//                }
            }

//            ui.
        }
    }

    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }
}
