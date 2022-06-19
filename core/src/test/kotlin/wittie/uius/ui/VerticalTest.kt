package wittie.uius.ui

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import wittie.uius.Dimensions2i
import wittie.uius.Point2i
import wittie.uius.Position2i

internal class VerticalTest {
    private fun nonNestedContainer(container: UiContainer, position: Position2i, children: List<Pair<Text, Position2i>>): PositionedContainer =
        PositionedContainer(container, position, listOf(), children, children)

    private fun nestedContainer(
        root: UiContainer,
        position: Position2i,
        childContainers: List<PositionedContainer>,
        childDrawables: List<Pair<Text, Position2i>>
    ) = PositionedContainer(root, position, childContainers, childDrawables,
        childContainers.flatMap { c -> c.descendantDrawables })

    @Test
    fun singleContainerShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fixed(125))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, listOf(Pair(d, p.withHeight(125)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun singleContainerWithTwoItemsShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fixed(75))
        vertical.add(d, Fixed(125))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, listOf(Pair(d, p.withHeight(75)), Pair(d, p.withHeight(125).withY(75)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun oneChildShouldFill() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, listOf(Pair(d, p.withHeight(200)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun twoChildrenShouldShareFill() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fill())
        vertical.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, listOf(Pair(d, p.withHeight(100)), Pair(d, p.withHeight(100).withY(100)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun threeChildrenShouldShareFillProportionally() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fill(25))
        vertical.add(d, Fill(60))
        vertical.add(d, Fill(15))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical,
                p,
                listOf(Pair(d, p.withHeight(50)), Pair(d, p.withHeight(120).withY(50)), Pair(d, p.withHeight(30).withY(170)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun singlePercentageShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(60))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, listOf(Pair(d, p.withHeight(120)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun percentageAndFillShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(60))
        vertical.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, listOf(
                    Pair(d, p.withHeight(120)),
                    Pair(d, p.withHeight(80).withY(120))
                )
            ), vertical.positioned(p)
        )
    }

    @Test
    fun percentageAndFixedShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(60))
        vertical.add(d, Fixed(50))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, listOf(
                    Pair(d, p.withHeight(120)),
                    Pair(d, p.withHeight(50).withY(120))
                )
            ), vertical.positioned(p)
        )
    }

    @Test
    fun fixedFillAndPercentageShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(15)) // 30
        vertical.add(d, Fixed(50)) // 50
        vertical.add(d, Fill(80)) // 200 - 80 * 2/3 = 120 * 2/3 = 80
        vertical.add(d, Fill(40))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(vertical, p, listOf(
                Pair(d, p.withHeight(30)),
                Pair(d, p.withHeight(50).withY(30)),
                Pair(d, p.withHeight(80).withY(80)),
                Pair(d, p.withHeight(40).withY(160))
            )),
            vertical.positioned(p)
        )
    }

    @Test
    fun nestedContainersShouldWork() {
        val d = Text("A")

        val child1 = Vertical()
        child1.add(d, Fill())

        val child2 = Vertical()
        child2.add(d, Fill())

        val parent = Vertical()
        parent.add(child1, Fill(75))
        parent.add(child2, Fill(25))

        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nestedContainer(
                parent,
                p,
                listOf(nonNestedContainer(
                        child1, Position2i(0, 0, 100, 150),
                        listOf(Pair(d, Position2i(0, 0, 100, 150)))
                    ), nonNestedContainer(
                        child2, Position2i(0, 150, 100, 50),
                        listOf(Pair(d, Position2i(0, 150, 100, 50)))
                    )
                ),
                listOf()
            ), parent.positioned(p)
        )
    }
}