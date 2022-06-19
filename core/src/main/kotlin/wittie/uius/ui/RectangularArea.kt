package wittie.uius.ui

import wittie.uius.Position2i

//class RectangularArea(val layouts: List<Layout> = listOf()) : UiContainer() {
class RectangularArea(val elements: List<UiElement> = listOf()) : UiContainer() {
    constructor(element: UiElement) : this(listOf(element))
//    constructor(background: Background, element: UiElement) : this(listOf(element, background.toRectangle()))
    constructor(background: Background, element: UiElement) : this(listOf(background.toRectangle(), element))
    constructor(
        background: Background,
        elements: List<UiElement> = listOf()
    ) : this(listOf(background.toRectangle()) + elements)
//    ) : this(elements + listOf(background.toRectangle()))

    override fun positioned(position: Position2i): PositionedContainer =
        elements.fold(PositionedContainer(this, position, listOf(), listOf(), listOf())) { positionedContainer, element ->
            when (element) {
                is UiContainer -> {
                    val childContainer = element.positioned(position)
                    positionedContainer.copy(
                        childContainers = positionedContainer.childContainers + childContainer,
                        descendantDrawables = positionedContainer.descendantDrawables + childContainer.descendantDrawables
                    )
                }
                is UiDrawable -> positionedContainer.copy(
                    descendantDrawables = positionedContainer.descendantDrawables + Pair(
                        element,
                        position
                    ),
                    childDrawables = positionedContainer.childDrawables + Pair(
                        element,
                        position
                    )
                )
            }
        }

    override fun type(): String {
        return "RectangularArea"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RectangularArea

        if (elements != other.elements) return false

        return true
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }
}