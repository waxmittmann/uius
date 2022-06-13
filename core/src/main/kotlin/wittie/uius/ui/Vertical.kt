package wittie.uius.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wittie.uius.Position2i
import wittie.uius.ShapeHelper

class Vertical : Layout() {
    private var children: MutableList<Pair<UiElement, FillBehavior>> = mutableListOf()

    fun add(content: UiElement, fill: FillBehavior) {
        children.add(Pair(content, fill))
    }

    override fun positioned(position: Position2i): PositionedContainer {
        val fixedTotal = LayoutFns.fixedSum(children.map { v -> v.second })
        val percentageTotal =
            ((LayoutFns.percentageSum(children.map { v -> v.second }) / 100.0) * position.dimensions.height).toInt()
        val fillWeightSum = LayoutFns.fillWeightSum(children.map { v -> v.second })

        data class Data(
            val yAt: Int = position.yMin(),
            val positionedContainer: PositionedContainer = PositionedContainer(this, position, listOf(), listOf(), listOf())
        )

        val rv = children.fold(Data()) { (yAt, container), (child, fill) ->
            val childPosition = when (fill) {
                is Fill ->
                    Position2i(
                        position.lowerLeft.copy(y = yAt),
                        position.dimensions.modify(heightFn = { height ->
                            ((fill.weight.toFloat() / fillWeightSum) * (height - fixedTotal - percentageTotal)).toInt()
                        })
                    )

                is Fixed ->
                    Position2i(
                        position.lowerLeft.copy(y = yAt),
                        position.dimensions.set(newHeight = fill.pixels)
                    )

                is Percentage -> {
                    val eleHeigth = ((fill.percentage / 100.0) * position.dimensions.height).toInt()

                    Position2i(
                        position.lowerLeft.set(newY = yAt),
                        position.dimensions.set(newHeight = eleHeigth)
                    )
                }
            }

            when (child) {
                is UiContainer -> {
                    val positionedChild = child.positioned(childPosition)
                    Data(
                        childPosition.yMax(),
                        // 'childDrawables' unchanged.
                        container.copy(
                            childContainers = container.childContainers + positionedChild,
                            descendantDrawables = container.descendantDrawables + positionedChild.descendantDrawables
                        )
                    )
                }
                is UiDrawable ->
                    Data(
                        childPosition.yMax(),
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

        other as Vertical

        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        return children.hashCode()
    }

}