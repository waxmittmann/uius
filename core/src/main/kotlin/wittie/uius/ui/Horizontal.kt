package wittie.uius.ui

import wittie.uius.Position2i

class Horizontal : Layout() {
    private var children: MutableList<Pair<UiElement, FillBehavior>> = mutableListOf()

    fun add(content: UiElement, fill: FillBehavior) {
        children.add(Pair(content, fill))
    }

    override fun positioned(position: Position2i): PositionedContainer {
        val fixedTotal = LayoutFns.fixedSum(children.map { v -> v.second })
        val percentageTotal =
            ((LayoutFns.percentageSum(children.map { v -> v.second }) / 100.0) * position.dimensions.width).toInt()
        val fillWeightSum = LayoutFns.fillWeightSum(children.map { v -> v.second })

        data class Data(
            val xAt: Int = position.xMin(),
            val positionedContainer: PositionedContainer = PositionedContainer(this, position, listOf(), listOf(), listOf())
        )

        val rv = children.fold(Data()) { (xAt, container), (child, fill) ->
            val childPosition = when (fill) {
                is Fill ->
                    Position2i(
                        position.lowerLeft.copy(x = xAt),
                        position.dimensions.modify(widthFn = { width ->
                            ((fill.weight.toFloat() / fillWeightSum) * (width - fixedTotal - percentageTotal)).toInt()
                        })
                    )

                is Fixed ->
                    Position2i(
                        position.lowerLeft.copy(x = xAt),
                        position.dimensions.set(newWidth = fill.pixels)
                    )

                is Percentage -> {
                    val eleWidth = ((fill.percentage / 100.0) * position.dimensions.width).toInt()

                    Position2i(
                        position.lowerLeft.set(newX = xAt),
                        position.dimensions.set(newWidth = eleWidth)
                    )
                }
            }

            when (child) {
                is UiContainer -> {
                    val positionedChild = child.positioned(childPosition)
                    Data(
                        childPosition.xMax(),
                        // 'childDrawables' unchanged.
                        container.copy(
                            childContainers = container.childContainers + positionedChild,
                            descendantDrawables = container.descendantDrawables + positionedChild.descendantDrawables
                        )
                    )
                }
                is UiDrawable ->
                    Data(
                        childPosition.xMax(),
                        container.copy(
                            descendantDrawables = container.descendantDrawables + Pair(child, childPosition),
                            childDrawables = container.childDrawables + Pair(child, childPosition)
                        )
                    )
            }
        }.positionedContainer
        return rv
    }

    override fun type(): String {
        return "Vertical"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Horizontal

        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        return children.hashCode()
    }

}