package wittie.uius.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wittie.uius.Position2i
import wittie.uius.ShapeHelper

class RectangularArea(val layouts: List<Layout> = listOf()) : UiContainer() {
    constructor(layout: Layout) : this(listOf(layout))
    constructor(background: Background, layout: Layout) : this(listOf(layout, toLayout(background)))
    constructor(background: Background, layouts: List<Layout> = listOf()) : this(listOf(toLayout(background)) + layouts)

    override fun childContainers(position: Position2i): List<PositionedContainer> {
        return layouts.flatMap { layout -> layout.childContainers(position) }
    }

    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {}

    override fun type(): String {
        return "RectangularArea"
    }
}