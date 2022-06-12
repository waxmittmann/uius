package wittie.uius.ui

object LayoutFns {
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