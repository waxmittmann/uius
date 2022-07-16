package wittie.uius.ui.components

import com.badlogic.gdx.graphics.Color
import wittie.uius.ui.ColorFill
import wittie.uius.ui.UiContainer
import wittie.uius.ui.elements.containers.Fill
import wittie.uius.ui.elements.containers.Fixed
import wittie.uius.ui.elements.containers.RectangularArea
import wittie.uius.ui.elements.containers.Table
import wittie.uius.ui.elements.drawables.Text

class MenuBar(menuElements: List<String>) {
//    val root = Table(listOf(Fixed(100), Fixed(400), Fill()), menuElements.map { Fixed(150) } + Fill())
//private val root = Table(listOf(Fill(), Fixed(400), Fixed(100)), menuElements.map { Fixed(150) } + Fill())
//    private val root = Table(menuElements.map { Fixed(150) } + Fill(), listOf(Fixed(100), Fixed(400), Fill()))
//    private val root = Table(menuElements.map { Fixed(150) } + Fill(), listOf(Fixed(500), Fill()))
    private val root = Table(listOf(Fill(), Fixed(100)), menuElements.map { Fixed(100) } + Fill(), false)

    init {
//        menuElements.zip(menuElements.indices).forEach { (text, index) ->
        menuElements.zip(listOf(Color.RED, Color.BLUE).withIndex()).forEach { (text, color) ->
            root.set(
                1, color.index, RectangularArea(
                    ColorFill(
                        color.value
                    ), Text(text)
                )
            )
        }
    }

    fun container(): UiContainer = root


}