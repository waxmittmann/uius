package wittie.uius.ui

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import wittie.uius.Dimensions2i
import wittie.uius.Point2i
import wittie.uius.Position2i
import wittie.uius.ui.elements.containers.Fill
import wittie.uius.ui.elements.containers.Fixed
import wittie.uius.ui.elements.containers.Horizontal
import wittie.uius.ui.elements.containers.Percentage
import wittie.uius.ui.elements.drawables.Text

internal class HorizontalTest {
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
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Fixed(125))
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(Pair(d, p.withWidth(125)))
            ), horizontal.positioned(p)
        )
    }
//
    @Test
    fun singleContainerWithTwoItemsShouldWork() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Fixed(75))
        horizontal.add(d, Fixed(125))
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(Pair(d, p.withWidth(75)), Pair(d, p.withWidth(125).withX(75)))
            ), horizontal.positioned(p)
        )
    }


    @Test
    fun oneChildShouldFill() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(Pair(d, p.withWidth(200)))
            ), horizontal.positioned(p)
        )
    }

    @Test
    fun twoChildrenShouldShareFill() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Fill())
        horizontal.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(Pair(d, p.withWidth(100)), Pair(d, p.withWidth(100).withX(100)))
            ), horizontal.positioned(p)
        )
    }

    @Test
    fun threeChildrenShouldShareFillProportionally() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Fill(25))
        horizontal.add(d, Fill(60))
        horizontal.add(d, Fill(15))
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal,
                p,
                listOf(Pair(d, p.withWidth(50)), Pair(d, p.withWidth(120).withX(50)), Pair(d, p.withWidth(30).withX(170)))
            ), horizontal.positioned(p)
        )
    }

    @Test
    fun singlePercentageShouldWork() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Percentage(60))
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(Pair(d, p.withWidth(120)))
            ), horizontal.positioned(p)
        )
    }

    @Test
    fun percentageAndFillShouldWork() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Percentage(60))
        horizontal.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(
                    Pair(d, p.withWidth(120)),
                    Pair(d, p.withWidth(80).withX(120))
                )
            ), horizontal.positioned(p)
        )
    }

    @Test
    fun percentageAndFixedShouldWork() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Percentage(60))
        horizontal.add(d, Fixed(50))
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(
                    Pair(d, p.withWidth(120)),
                    Pair(d, p.withWidth(50).withX(120))
                )
            ), horizontal.positioned(p)
        )
    }

    @Test
    fun fixedFillAndPercentageShouldWork() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Percentage(15)) // 30
        horizontal.add(d, Fixed(50)) // 50
        horizontal.add(d, Fill(80)) // 200 - 80 * 2/3 = 120 * 2/3 = 80
        horizontal.add(d, Fill(40))
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(horizontal, p, listOf(
                Pair(d, p.withWidth(30)),
                Pair(d, p.withWidth(50).withX(30)),
                Pair(d, p.withWidth(80).withX(80)),
                Pair(d, p.withWidth(40).withX(160))
            )),
            horizontal.positioned(p)
        )
    }

    @Test
    fun nestedContainersShouldWork() {
        val d = Text("A")

        val child1 = Horizontal()
        child1.add(d, Fill())

        val child2 = Horizontal()
        child2.add(d, Fill())

        val parent = Horizontal()
        parent.add(child1, Fill(75))
        parent.add(child2, Fill(25))

        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nestedContainer(
                parent,
                p,
                listOf(nonNestedContainer(
                    child1, Position2i(0, 0, 150, 100),
                    listOf(Pair(d, Position2i(0, 0, 150, 100)))
                ), nonNestedContainer(
                    child2, Position2i(150, 0, 50, 100),
                    listOf(Pair(d, Position2i(150, 0, 50, 100)))
                )
                ),
                listOf()
            ), parent.positioned(p)
        )
    }
//
}