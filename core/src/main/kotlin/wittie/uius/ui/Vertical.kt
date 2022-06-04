package wittie.uius.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wittie.uius.Position2i
import wittie.uius.ShapeHelper

class Vertical : Layout() {
    private var elements: MutableList<UiDrawableWithFill> = mutableListOf()

    fun add(content: UiContainer, fill: FillBehavior) {
        elements.add(UiDrawableWithFill(content, fill))
    }

    override fun childContainers(position: Position2i): List<PositionedContainer> {
        if(elements.isEmpty())
            return listOf()

        val fixedTotal = LayoutFns.fixedSum(elements)
        val percentageTotal = ((LayoutFns.percentageSum(elements) / 100.0) * position.dimensions.height).toInt()
        val fillWeightSum = LayoutFns.fillWeightSum(elements)

        val result =  elements.fold<UiDrawableWithFill, Pair<List<PositionedContainer>, Int>>(Pair(listOf(), position.lowerLeft.y)) { (elementsSoFar, yAt), element ->
            when (element.fill) {
                is Fill -> {
                    val resultElements = element.drawable.childContainers(
                        Position2i(
                            position.lowerLeft.copy(y = yAt),
                            position.dimensions.modify(heightFn = { height -> ((element.fill.weight.toFloat() / fillWeightSum) * (height - fixedTotal - percentageTotal)).toInt() })
                        )
                    )

                    Pair(
                        elementsSoFar + resultElements,
                        if (resultElements.isEmpty()) yAt else resultElements.last().position.yMax()
                    )
                }

                is Fixed -> {
                    val resultElement = PositionedContainer(
                        element.drawable,
                        Position2i(
                            position.lowerLeft.set(newY = yAt),
                            position.dimensions.set(newHeight = element.fill.pixels)
                        )
                    )
                    Pair(elementsSoFar + resultElement, yAt + element.fill.pixels)
                }

                is Percentage -> {
//                    println("$yAt  $fixedTotal $percentageTotal")
                    val eleHeigth = ((element.fill.percentage  / 100.0) * position.dimensions.height).toInt()
                    val resultElement = PositionedContainer(
                        element.drawable,
                        Position2i(
                            position.lowerLeft.set(newY = yAt),
                            position.dimensions.set(newHeight = eleHeigth)
                        )
                    )
                    Pair(elementsSoFar + resultElement, yAt + eleHeigth)
                }
            }
        }.first
//        assert(result.size == elements.size)
        return result
    }

    override fun positioned(position: Position2i): PositionedContainer {


    }

    /*
        fun visit(visitData: S): List<Pair<UiDrawable, S>> {

        }

     */


    override fun <S, T, U> visit(s: S): List<S> {

    }


    override fun <S, T, U> visit(s: S): List<S> {

//        elements.fold<T, Pair<

        if(elements.isEmpty())
            return listOf()

        val fixedTotal = LayoutFns.fixedSum(elements)
        val percentageTotal = ((LayoutFns.percentageSum(elements) / 100.0) * position.dimensions.height).toInt()
        val fillWeightSum = LayoutFns.fillWeightSum(elements)

        val result =  elements.fold<UiDrawableWithFill, Pair<List<PositionedContainer>, Int>>(Pair(listOf(), position.lowerLeft.y)) { (elementsSoFar, yAt), element ->
            when (element.fill) {
                is Fill -> {
                    val resultElements = element.drawable.childContainers(
                        Position2i(
                            position.lowerLeft.copy(y = yAt),
                            position.dimensions.modify(heightFn = { height -> ((element.fill.weight.toFloat() / fillWeightSum) * (height - fixedTotal - percentageTotal)).toInt() })
                        )
                    )

                    Pair(
                        elementsSoFar + resultElements,
                        if (resultElements.isEmpty()) yAt else resultElements.last().position.yMax()
                    )
                }

                is Fixed -> {
                    val resultElement = PositionedContainer(
                        element.drawable,
                        Position2i(
                            position.lowerLeft.set(newY = yAt),
                            position.dimensions.set(newHeight = element.fill.pixels)
                        )
                    )
                    Pair(elementsSoFar + resultElement, yAt + element.fill.pixels)
                }

                is Percentage -> {
//                    println("$yAt  $fixedTotal $percentageTotal")
                    val eleHeigth = ((element.fill.percentage  / 100.0) * position.dimensions.height).toInt()
                    val resultElement = PositionedContainer(
                        element.drawable,
                        Position2i(
                            position.lowerLeft.set(newY = yAt),
                            position.dimensions.set(newHeight = eleHeigth)
                        )
                    )
                    Pair(elementsSoFar + resultElement, yAt + eleHeigth)
                }
            }
        }.first
//        assert(result.size == elements.size)
        return result
    }

    override fun drawContent(batch: SpriteBatch, helper: ShapeHelper, position: Position2i) {}
    override fun type(): String {
        return "Vertical"
    }
}