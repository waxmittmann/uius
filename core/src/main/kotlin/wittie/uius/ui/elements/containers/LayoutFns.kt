package wittie.uius.ui.elements.containers

import wittie.uius.Dimensions2i

data class LayoutTotals(val totalSize: Int, val fixedTotal: Int, val percentageTotal: Int, val fillWeightSum: Int) {
    fun size(fill: FillBehavior): Int =
        when (fill) {
            is Fill -> ((fill.weight.toFloat() / fillWeightSum) * (totalSize - fixedTotal - percentageTotal)).toInt()
            is Fixed -> fill.pixels
            is Percentage -> ((fill.percentage / 100.0) * totalSize).toInt()
        }

}
data class LayoutTotals2d(val horizontalTotals: LayoutTotals, val verticalTotals: LayoutTotals) {
    fun size(horizontalFill: FillBehavior, verticalFill: FillBehavior): Dimensions2i =
        Dimensions2i(horizontalTotals.size(horizontalFill), verticalTotals.size(verticalFill))

}

object LayoutFns {
//    fun totals2d(width: Int, height: Int, horizontalFills: List<FillBehavior>, verticalFills: List<FillBehavior>): LayoutTotals2d {
////        return LayoutTotals2d(totals(width, verticalFills), totals(height, horizontalFills))
//        return LayoutTotals2d(totals(width, verticalFills), totals(height, horizontalFills))
//    }

    fun totals2d(width: Int, height: Int, colFills: List<FillBehavior>, rowFills: List<FillBehavior>): LayoutTotals2d {
//        return LayoutTotals2d(totals(width, verticalFills), totals(height, horizontalFills))
        return LayoutTotals2d(totals(width, colFills), totals(height, rowFills))
    }
    fun totals(totalSize: Int, elements: List<FillBehavior>): LayoutTotals =
        LayoutTotals(
            totalSize,
            fixedSum(elements),
            ((percentageSum(elements) / 100.0) * totalSize).toInt(),
            fillWeightSum(elements)
        )

    fun fixedSum(elements: List<FillBehavior>): Int =
        elements.flatMap { element ->
            when (element) {
                is Fill -> listOf()
                is Fixed -> listOf(element.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

    fun percentageSum(elements: List<FillBehavior>): Int =
        elements.map { element ->
            when (element) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.percentage
            }
        }.fold(0) { a, b -> a + b }

    fun fillWeightSum(elements: List<FillBehavior>): Int =
        elements.map { element ->
            when (element) {
                is Fill -> element.weight
                is Fixed -> 0
                is Percentage -> 0
            }
        }.fold(0) { a, b -> a + b }
}