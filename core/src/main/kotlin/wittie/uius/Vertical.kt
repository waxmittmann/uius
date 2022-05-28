package wittie.uius

import com.badlogic.gdx.graphics.g2d.Batch

class Vertical : Layout() {
    private var elements: MutableList<UiDrawableWithFill> = mutableListOf()

    fun add(content: UiDrawable, fill: FillBehavior) {
        elements.add(UiDrawableWithFill(content, fill))
    }

    override fun children(position: Position2i): List<PositionedDrawable> {
        if(elements.isEmpty())
            return listOf()

        val fixedTotal = LayoutFns.fixedSum(elements)
        val percentageTotal = LayoutFns.percentageSum(elements) * (position.dimensions.height - fixedTotal)
        val fillWeightSum = LayoutFns.fillWeightSum(elements)

        val result =  elements.fold<UiDrawableWithFill, Pair<List<PositionedDrawable>, Int>>(Pair(listOf(), position.lowerLeft.y)) { (elementsSoFar, yAt), element ->
            when (element.fill) {
                is Fill -> {
                    println("$yAt ${element.fill.weight.toFloat()} $fillWeightSum $fixedTotal $percentageTotal")
                    val resultElements = element.drawable.children(
                        Position2i(
                            position.lowerLeft.set(newY = yAt),
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
                    val eleHeigth = element.fill.percentage * percentageTotal
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
        assert(result.size == elements.size)
        return result
    }



    override fun draw(batch: Batch, dims: Dimensions2i) {
        if(elements.isEmpty())
            return
        var total = 0
//        element.element.draw(batch, Dimensions2i(dims.width, element.fill.pixels))

        val fixedTotal = elements.flatMap { element ->
            when(element.fill) {
                is Fill -> listOf()
                is Fixed -> listOf(element.fill.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

        val percentageTotal = elements.map { element ->
            when(element.fill) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.fill.percentage
            }
        }.fold(0){ a, b  -> a + b } * (dims.height - fixedTotal)

        val fillTotal = elements.map { element ->
            when(element.fill) {
                is Fill -> element.fill.weight
                is Fixed -> 0
                is Percentage -> 0
            }
        }.fold(0) { a, b -> a + b }

        elements.forEach { element ->
            when(element.fill) {
                is Fill -> element.drawable.draw(batch,
                    Dimensions2i(
                        dims.width,
                        element.fill.weight / fillTotal * (dims.height - fixedTotal - percentageTotal)
                    )
                )
                is Fixed -> element.drawable.draw(batch, Dimensions2i(dims.width, element.fill.pixels))
                is Percentage -> element.drawable.draw(batch,
                    Dimensions2i(dims.width, element.fill.percentage * (dims.height - fixedTotal))
                )
            }
        }
    }
}