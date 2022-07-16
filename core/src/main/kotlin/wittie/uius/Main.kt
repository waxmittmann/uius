package wittie.uius

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile


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

//    private val uiAndEvents = ui()
    private val uiAndEvents = ui2()
    private val helper = ShapeHelper(ShapeRenderer())

    private var windowSize = Dimensions2i(0, 0)
    private var drawables = uiAndEvents.first.positioned(Position2i(Point2i(0, 0), windowSize)).descendantDrawables

    val camera: OrthographicCamera = OrthographicCamera()

    init {
        windowSize = Dimensions2i(Gdx.graphics.width, Gdx.graphics.height)
        drawables = uiAndEvents.first.positioned(Position2i(Point2i(0, 0), windowSize)).descendantDrawables
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.update()


//        Ktx
        Gdx.input.inputProcessor = (object : KtxInputAdapter {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                println("Touch down!")
                // Note we do not currently adjust for camera, so UI has to be 1-to-1 with screen size.
                uiAndEvents.second.triggerEventsAt(
                    Point2i(screenX, windowSize.height - screenY),
                    uiAndEvents.first.positioned(Position2i(Point2i(0, 0), windowSize))
                )
                return false
            }
        })
    }


    override fun render(delta: Float) {
        time += delta

        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        camera.update()
        batch.projectionMatrix = camera.combined
        helper.setProjectionMatrix(camera.combined)
        //        if ()

        val drawables = uiAndEvents.first.positioned(Position2i(Point2i(0, 0), windowSize)).descendantDrawables
        drawables.forEach { (drawable, pos) -> drawable.drawContent(batch, helper, pos) }

        lastTime = time
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        println("Size is " + width + ", " + height)
        windowSize = Dimensions2i(width, height)
        drawables = uiAndEvents.first.positioned(Position2i(Point2i(0, 0), windowSize)).descendantDrawables
        camera.setToOrtho(false, width.toFloat(), height.toFloat())
        camera.update()
    }

    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }
}
