package wittie.uius

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Vertical : Layout() {
    private var elements: MutableList<UiDrawableWithFill> = mutableListOf()

    fun add(content: UiDrawable, fill: FillBehavior) {
        elements.add(UiDrawableWithFill(content, fill))
    }

    override fun children(position: Position2i): List<PositionedDrawable> {
        if(elements.isEmpty())
            return listOf()

        val fixedTotal = LayoutFns.fixedSum(elements)
        val percentageTotal = ((LayoutFns.percentageSum(elements) / 100.0) * position.dimensions.height).toInt()
//        val nonFixedTotal = position.dimensions.height - fixedTotal
//        val percentageTotal = ((LayoutFns.percentageSum(elements) / 100.0) * (position.dimensions.height - fixedTotal)).toInt()
        val fillWeightSum = LayoutFns.fillWeightSum(elements)

        val result =  elements.fold<UiDrawableWithFill, Pair<List<PositionedDrawable>, Int>>(Pair(listOf(), position.lowerLeft.y)) { (elementsSoFar, yAt), element ->
            when (element.fill) {
                is Fill -> {
                    val resultElements = element.drawable.children(
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
                    val resultElement = PositionedDrawable(
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
                    val resultElement = PositionedDrawable(
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