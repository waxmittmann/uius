package wittie.uius.ui

object LayoutFns {
    fun fixedSum(elements: List<UiDrawableWithFill>): Int =
        elements.flatMap { element ->
            when (element.fill) {
                is Fill -> listOf()
                is Fixed -> listOf(element.fill.pixels)
                is Percentage -> listOf()
            }
        }.fold(0) { a, b -> a + b }

    fun percentageSum(elements: List<UiDrawableWithFill>): Int =
        elements.map { element ->
            when (element.fill) {
                is Fill -> 0
                is Fixed -> 0
                is Percentage -> element.fill.percentage
            }
        }.fold(0) { a, b -> a + b }

    fun fillWeightSum(elements: List<UiDrawableWithFill>): Int =
        elements.map { element ->
            when (element.fill) {
                is Fill -> element.fill.weight
                is Fixed -> 0
                is Percentage -> 0
            }
        }.fold(0) { a, b -> a + b }
}