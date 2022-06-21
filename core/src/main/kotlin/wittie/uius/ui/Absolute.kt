package wittie.uius.ui

import wittie.uius.Position2i

class Absolute(private val children: MutableList<Pair<UiElement, Position2i>> = mutableListOf()) : Layout() {

    fun add(element: UiElement, pos: Position2i) {
        children.add(Pair(element, pos))
    }

    override fun positioned(containerPosition: Position2i): PositionedContainer =
        children.fold(PositionedContainer(this, containerPosition, listOf(), listOf(), listOf())) { pc: PositionedContainer, (child: UiElement, childPosition) ->
            val relativePosition = childPosition.copy(lowerLeft = childPosition.lowerLeft.add(containerPosition.lowerLeft))
            when(child) {
                is UiDrawable -> pc.addDrawable(child, relativePosition)
                is UiContainer -> pc.addContainer(child.positioned(relativePosition))
            }
        }

    override fun type(): String = "Absolute"
}