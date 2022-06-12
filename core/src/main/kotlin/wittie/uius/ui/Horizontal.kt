//package wittie.uius.ui
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch
//import wittie.uius.Position2i
//import wittie.uius.ShapeHelper
//
//class Horizontal : Layout() {
//    //    private var elements: MutableList<Pair<Element, FillBehavior>> = mutableListOf()
//    private var elements: MutableList<UiDrawableWithFill> = mutableListOf()
//
//    fun add(content: UiContainer, fill: FillBehavior) {
//        elements.add(UiDrawableWithFill(content, fill))
//    }
//
//    override fun childContainers(position: Position2i): List<PositionedContainer> {
//        if (elements.isEmpty())
//            return listOf()
//
//        val fixedTotal = elements.flatMap { element ->
//            when (element.fill) {
//                is Fill -> listOf()
//                is Fixed -> listOf(element.fill.pixels)
//                is Percentage -> listOf()
//            }
//        }.fold(0) { a, b -> a + b }
//
//        val percentageTotal = elements.map { element ->
//            when (element.fill) {
//                is Fill -> 0
//                is Fixed -> 0
//                is Percentage -> element.fill.percentage
//            }
//        }.fold(0) { a, b -> a + b } * (position.dimensions.height - fixedTotal)
//
//        val fillTotal = elements.map { element ->
//            when (element.fill) {
//                is Fill -> element.fill.weight
//                is Fixed -> 0
//                is Percentage -> 0
//            }
//        }.fold(0) { a, b -> a + b }
//
//        return elements.fold<UiDrawableWithFill, Pair<List<PositionedContainer>, Int>>(
//            Pair(
//                listOf(),
//                position.lowerLeft.x
//            )
//        ) { (elementsSoFar, xAt), element ->
//            when (element.fill) {
//                is Fill -> {
//                    val resultElement = element.drawable.childContainers(
//                        Position2i(
//                            position.lowerLeft.set(newX = xAt),
//                            position.dimensions.modify(widthFn = { width -> xAt + element.fill.weight / fillTotal * (width - fixedTotal - percentageTotal) })
//                        )
//                    )
//                    Pair(
//                        elementsSoFar + resultElement,
//                        if (resultElement.isEmpty()) xAt else xAt + resultElement.last().position.lowerLeft.x + resultElement.last().position.dimensions.width
//                    )
//                }
//
//                is Fixed -> {
//                    val resultElement = PositionedContainer(
//                        element.drawable,
//                        Position2i(
//                            position.lowerLeft.set(newX = xAt),
//                            position.dimensions.set(newWidth = element.fill.pixels)
//                        )
//                    )
//                    Pair(elementsSoFar + resultElement, xAt + element.fill.pixels)
//                }
//
//                is Percentage -> {
//                    val eleWidth = element.fill.percentage * percentageTotal
//                    val resultElement = PositionedContainer(
//                        element.drawable,
//                        Position2i(
//                            position.lowerLeft.set(newX = xAt),
//                            position.dimensions.set(newWidth = eleWidth)
//                        )
//                    )
//                    Pair(elementsSoFar + resultElement, xAt + eleWidth)
//                }
//            }
//        }.first
//    }
//
//    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {}
//
//    override fun type(): String {
//        return "Horizontal"
//    }
//}