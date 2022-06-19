package wittie.uius.ui

import wittie.uius.Position2i

class Absolute(private val children: MutableList<Pair<UiElement, Position2i>> = mutableListOf()) : Layout() {

    fun add(element: UiElement, pos: Position2i) {
        children.add(Pair(element, pos))
    }

    override fun positioned(position: Position2i): PositionedContainer =
        children.fold(PositionedContainer(this, position, listOf(), listOf(), listOf())) { pc: PositionedContainer, (child, position) ->
            when(child) {
                is UiDrawable -> pc.addDrawable(child, position)
                is UiContainer -> pc.addContainer(child.positioned(position))
            }
        }

    override fun type(): String = "Absolute"
}